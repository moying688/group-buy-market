package com.moying.infrastructure.adapter.repository;

import com.alibaba.fastjson2.JSON;
import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.moying.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import com.moying.domain.trade.model.entity.*;
import com.moying.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import com.moying.domain.trade.model.entity.TradeRefundOrderEntity;
import com.moying.domain.trade.model.valobj.*;
import com.moying.infrastructure.dao.IGroupBuyActivityDao;
import com.moying.infrastructure.dao.IGroupBuyOrderDao;
import com.moying.infrastructure.dao.IGroupBuyOrderListDao;
import com.moying.infrastructure.dao.INotifyTaskDao;
import com.moying.infrastructure.dao.po.GroupBuyActivity;
import com.moying.infrastructure.dao.po.GroupBuyOrder;
import com.moying.infrastructure.dao.po.GroupBuyOrderList;
import com.moying.infrastructure.dao.po.NotifyTask;
import com.moying.infrastructure.dcc.DCCService;
import com.moying.infrastructure.redis.IRedisService;
import com.moying.types.common.Constants;
import com.moying.types.enums.ActivityStatusEnumVO;
import com.moying.types.enums.GroupBuyOrderEnumVO;
import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 交易仓储服务接口
 */


@Repository
@Slf4j
public class TradeRepository implements ITradeRepository {

    @Resource
    private IGroupBuyOrderDao groupBuyOrderDao;
    @Resource
    private IGroupBuyOrderListDao groupBuyOrderListDao;

    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;

    @Resource
    private INotifyTaskDao notifyTaskDao;

    @Resource
    private DCCService dccService;

    @Value("${mq.producer.topic.team-success}")
    private String topic;

    @Value("${mq.producer.topic.team-refund}")
    private String topic_team_refund;

    @Resource
    private IRedisService redisService;

    @Override
    @Transactional(timeout = 500)
    public MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate) {
        // 聚合对象信息
        UserEntity userEntity = groupBuyOrderAggregate.getUserEntity();
        PayActivityEntity payActivityEntity = groupBuyOrderAggregate.getPayActivityEntity();
        PayDiscountEntity payDiscountEntity = groupBuyOrderAggregate.getPayDiscountEntity();
        Integer userTakeOrderCount = groupBuyOrderAggregate.getUserTakeOrderCount();
        NotifyConfigVO notifyConfigVO = payDiscountEntity.getNotifyConfigVO();

        // 判断是否有团 - teamId 为空 -> 新团、不为空 -> 老团
        String teamId = payActivityEntity.getTeamId();
        if (StringUtils.isBlank(teamId)) {
            teamId = RandomStringUtils.randomNumeric(8);


            // 日期处理
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.MINUTE, payActivityEntity.getValidTime());
//            // 直接在毫秒上加分钟
//            long offsetMillis = TimeUnit.MINUTES.toMillis(payActivityEntity.getValidTime());
//            Date newDate = new Date(currentDate.getTime() + offsetMillis);

            // 构建拼团订单
            GroupBuyOrder groupBuyOrder = GroupBuyOrder.builder()
                    .teamId(teamId)
                    .activityId(payActivityEntity.getActivityId())
                    .source(payDiscountEntity.getSource())
                    .channel(payDiscountEntity.getChannel())
                    .originalPrice(payDiscountEntity.getOriginalPrice())
                    .deductionPrice(payDiscountEntity.getDeductionPrice())
                    .payPrice(payDiscountEntity.getPayPrice())
                    .targetCount(payActivityEntity.getTargetCount())
                    .validStartTime(currentDate)
                    .validEndTime(calendar.getTime())
                    .completeCount(0)
                    .lockCount(1)
                    .notifyUrl(notifyConfigVO.getNotifyUrl())
                    .notifyType(notifyConfigVO.getNotifyType().getCode())
                    .build();

            // 写入订单记录
            groupBuyOrderDao.insert(groupBuyOrder);
        } else {
            // 更新记录 - 如果更新记录不等于1，则表示拼团已满，抛出异常
            int updateAddTargetCount = groupBuyOrderDao.updateAddLockCount(teamId);
            if (1 != updateAddTargetCount) {
                throw new AppException(ResponseCode.E0005);
            }
        }

        // 构建订单记录
        String orderId = RandomStringUtils.randomNumeric(12);
        GroupBuyOrderList groupBuyOrderList = GroupBuyOrderList.builder()
                .userId(userEntity.getUserId())
                .teamId(teamId)
                .orderId(orderId)
                .activityId(payActivityEntity.getActivityId())
                .startTime(payActivityEntity.getStartTime())
                .endTime(payActivityEntity.getEndTime())
                .goodsId(payDiscountEntity.getGoodsId())
                .source(payDiscountEntity.getSource())
                .channel(payDiscountEntity.getChannel())
                .originalPrice(payDiscountEntity.getOriginalPrice())
                .deductionPrice(payDiscountEntity.getDeductionPrice())
                .payPrice(payDiscountEntity.getPayPrice())
                .status(TradeOrderStatusEnumVO.CREATE.getCode())
                .outTradeNo(payDiscountEntity.getOutTradeNo())
                // 构建 bizId 唯一值；活动id_用户id_参与次数累加
                .bizId(payActivityEntity.getActivityId() + Constants.UNDERLINE + userEntity.getUserId() + Constants.UNDERLINE + (userTakeOrderCount + 1))
                .build();

        try {
            // 写入订单记录
            groupBuyOrderListDao.insert(groupBuyOrderList);
        } catch (DuplicateKeyException e) {
            throw new AppException(ResponseCode.INDEX_EXCEPTION);
        }

        return MarketPayOrderEntity.builder()
                .orderId(orderId)
                .deductionPrice(groupBuyOrderList.getDeductionPrice())
                .payPrice(groupBuyOrderList.getPayPrice())
                .originalPrice(groupBuyOrderList.getDeductionPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.CREATE)
                .build();
    }

    @Override
    public MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo) {
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOutTradeNo(outTradeNo);

        GroupBuyOrderList groupBuyOrderList = groupBuyOrderListDao
                .queryGroupBuyOrderRecordByOutTradeNo(groupBuyOrderListReq);
        if (null == groupBuyOrderList) return null;
        return MarketPayOrderEntity.builder()
                .teamId(groupBuyOrderList.getTeamId())
                .orderId(groupBuyOrderList.getOrderId())
                .deductionPrice(groupBuyOrderList.getDeductionPrice())
                .payPrice(groupBuyOrderList.getPayPrice())
                .originalPrice(groupBuyOrderList.getOriginalPrice())
                .tradeOrderStatusEnumVO(TradeOrderStatusEnumVO.valueOf(groupBuyOrderList.getStatus()))
                .build();
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyProgress(teamId);
        if (null == groupBuyOrder) return null;
        return GroupBuyProgressVO.builder()
                .completeCount(groupBuyOrder.getCompleteCount())
                .targetCount(groupBuyOrder.getTargetCount())
                .lockCount(groupBuyOrder.getLockCount())
                .build();
    }

    @Override
    public GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId) {
        GroupBuyActivity groupBuyActivity = groupBuyActivityDao.queryGroupBuyActivityByActivityId(activityId);
        return GroupBuyActivityEntity.builder()
                .activityId(groupBuyActivity.getActivityId())
                .activityName(groupBuyActivity.getActivityName())
                .discountId(groupBuyActivity.getDiscountId())
                .groupType(groupBuyActivity.getGroupType())
                .takeLimitCount(groupBuyActivity.getTakeLimitCount())
                .target(groupBuyActivity.getTarget())
                .validTime(groupBuyActivity.getValidTime())
                .status(ActivityStatusEnumVO.valueOf(groupBuyActivity.getStatus()))
                .startTime(groupBuyActivity.getStartTime())
                .endTime(groupBuyActivity.getEndTime())
                .tagId(groupBuyActivity.getTagId())
                .tagScope(groupBuyActivity.getTagScope())
                .build();
    }

    @Override
    public Integer queryOrderCountByActivityId(Long activityId, String userId) {
        GroupBuyOrderList groupBuyOrderList = new GroupBuyOrderList();
        groupBuyOrderList.setActivityId(activityId);
        groupBuyOrderList.setUserId(userId);

        return groupBuyOrderListDao.queryOrderCountByActivityId(groupBuyOrderList);
    }

    @Override
    public GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId) {
        GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyTeamByTeamId(teamId);

        return GroupBuyTeamEntity.builder()
                .teamId(groupBuyOrder.getTeamId())
                .activityId(groupBuyOrder.getActivityId())
                .targetCount(groupBuyOrder.getTargetCount())
                .completeCount(groupBuyOrder.getCompleteCount())
                .lockCount(groupBuyOrder.getLockCount())
                .status(GroupBuyOrderEnumVO.valueOf(groupBuyOrder.getStatus()))
                .validStartTime(groupBuyOrder.getValidStartTime())
                .validEndTime(groupBuyOrder.getValidEndTime())
                .notifyConfigVO(NotifyConfigVO.builder()
                        .notifyType(NotifyTypeEnumVO.valueOf(groupBuyOrder.getNotifyType()))
                        .notifyUrl(groupBuyOrder.getNotifyUrl())
                        // MQ 是固定的
                        .notifyMQ(topic)
                        .build())
                .build();
    }

    @Override
    @Transactional(timeout = 5000)
    public NotifyTaskEntity settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate) {
        UserEntity userEntity = groupBuyTeamSettlementAggregate.getUserEntity();
        GroupBuyTeamEntity groupBuyTeamEntity = groupBuyTeamSettlementAggregate.getGroupBuyTeamEntity();
        TradePaySuccessEntity tradePaySuccessEntity = groupBuyTeamSettlementAggregate.getTradePaySuccessEntity();
        NotifyConfigVO notifyConfigVO = groupBuyTeamEntity.getNotifyConfigVO();

        // 1. 更新拼团订单明细状态
        GroupBuyOrderList groupBuyOrderList = new GroupBuyOrderList();
        groupBuyOrderList.setUserId(userEntity.getUserId());
        groupBuyOrderList.setOutTradeNo(tradePaySuccessEntity.getOutTradeNo());
        groupBuyOrderList.setOutTradeTime(new Date());
        int updateOrderListStatusCount = groupBuyOrderListDao.updateOrderStatusToCOMPLETE(groupBuyOrderList);

        if (1 != updateOrderListStatusCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 2. 更新拼团订单状态
        int updateAddCount = groupBuyOrderDao.updateAddCompleteCount(groupBuyTeamEntity.getTeamId());
        if (1 != updateAddCount) {
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 3. 更新拼团完成状态(如果是最后一单完成)
        if (groupBuyTeamEntity.getTargetCount() - groupBuyTeamEntity.getCompleteCount() == 1) {
            int updateOrderStatusCount = groupBuyOrderDao.updateOrderStatus2COMPLETE(groupBuyTeamEntity.getTeamId());
            if (1 != updateOrderStatusCount) {
                throw new AppException(ResponseCode.UPDATE_ZERO);
            }

            // 查询拼团交易完成外部单号列表
            List<String> outTradeNoList = groupBuyOrderListDao.queryGroupBuyCompleteOrderOutTradeNoListByTeamId(groupBuyTeamEntity.getTeamId());
            // 4. 写入通知任务
            NotifyTask notifyTask = new NotifyTask();
            notifyTask.setActivityId(groupBuyTeamEntity.getActivityId());
            notifyTask.setTeamId(groupBuyTeamEntity.getTeamId());
            notifyTask.setNotifyType(notifyConfigVO.getNotifyType().getCode());
            notifyTask.setNotifyMQ(NotifyTypeEnumVO.MQ.equals(notifyConfigVO.getNotifyType()) ? notifyConfigVO.getNotifyMQ() : null);
            notifyTask.setNotifyUrl(NotifyTypeEnumVO.HTTP.equals(notifyConfigVO.getNotifyType()) ? notifyConfigVO.getNotifyUrl() : null);
            notifyTask.setNotifyCount(0);
            notifyTask.setNotifyStatus(0);
            notifyTask.setParameterJson(JSON.toJSONString(new HashMap<String, Object>() {{
                put("teamId", groupBuyTeamEntity.getTeamId());
                put("outTradeNoList", outTradeNoList);
            }}));

            notifyTaskDao.insert(notifyTask);

            return NotifyTaskEntity.builder()
                    .teamId(notifyTask.getTeamId())
                    .notifyType(notifyTask.getNotifyType())
                    .notifyMQ(notifyTask.getNotifyMQ())
                    .notifyUrl(notifyTask.getNotifyUrl())
                    .notifyCount(notifyTask.getNotifyCount())
                    .parameterJson(notifyTask.getParameterJson())
                    .build();
        }
        return null;
    }

    @Override
    public boolean isSCBlackIntercept(String source, String channel) {
        return dccService.isSCBlackIntercept(source, channel);
    }

    @Override
    public List<NotifyTaskEntity> queryUnExecutedNotifyTaskList() {
       List<NotifyTask> notifyTasks = notifyTaskDao.queryUnExecutedNotifyTaskList();
        if (notifyTasks.isEmpty()) return new ArrayList<>();
      List<NotifyTaskEntity> notifyTaskEntityList = new ArrayList<>();

      for (NotifyTask notifyTask : notifyTasks) {
          NotifyTaskEntity notifyTaskEntity = NotifyTaskEntity.builder()
                  .teamId(notifyTask.getTeamId())
                  .notifyType(notifyTask.getNotifyType())
                  .notifyMQ(notifyTask.getNotifyMQ())
                  .notifyUrl(notifyTask.getNotifyUrl())
                  .notifyCount(notifyTask.getNotifyCount())
                  .parameterJson(notifyTask.getParameterJson())
                  .build();
          notifyTaskEntityList.add(notifyTaskEntity);
      }
      return notifyTaskEntityList;
    }

    @Override
    public List<NotifyTaskEntity> queryUnExecutedNotifyTaskList(String teamId) {
        NotifyTask notifyTask = notifyTaskDao.queryUnExecutedNotifyTaskByTeamId(teamId);
        if (null == notifyTask) return new ArrayList<>();
        return Collections.singletonList(NotifyTaskEntity.builder()
                .teamId(notifyTask.getTeamId())
                .notifyType(notifyTask.getNotifyType())
                .notifyMQ(notifyTask.getNotifyMQ())
                .notifyUrl(notifyTask.getNotifyUrl())
                .notifyCount(notifyTask.getNotifyCount())
                .parameterJson(notifyTask.getParameterJson())
                .build());
    }

    @Override
    public int updateNotifyTaskStatusSuccess(String teamId) {
        return notifyTaskDao.updateNotifyTaskStatusSuccess(teamId);
    }

    @Override
    public int updateNotifyTaskStatusError(String teamId) {
        return notifyTaskDao.updateNotifyTaskStatusError(teamId);
    }

    @Override
    public int updateNotifyTaskStatusRetry(String teamId) {
        return  notifyTaskDao.updateNotifyTaskStatusRetry(teamId);
    }

    @Override
    public boolean queryGroupBuyTeamByUserIdAndTeamId(String userId, String teamId) {
       int count = groupBuyOrderListDao.queryGroupBuyTeamByUserIdAndTeamId(userId, teamId);
        return count > 0;
    }

    @Override
    public boolean occupyTeamStock(String teamStockKey, String recoveryTeamStockKey,String userLockKey, Integer target, Integer validTime) {
       // 用户幂等处理
        Boolean userLock = redisService.setNx(userLockKey, validTime + 60, TimeUnit.MINUTES);
        if (!userLock) return false;


        // 先从队列中获取恢复 量
        Integer recoveryCount = redisService.lPop(recoveryTeamStockKey);

        if(recoveryCount == null){
            long occupy  = redisService.incr(teamStockKey) + 1;
            if(occupy > target){
                return false;
            }
        }
        // todo 特殊情况再采用锁兜底
        return true;
    }

    @Override
    public void recoveryTeamStock(String recoveryTeamStockKey, Integer validTime) {
        // 首次组队拼团，是没有 teamId 的，所以不需要这个做处理。
        if (StringUtils.isBlank(recoveryTeamStockKey)) return;
        redisService.rPush(recoveryTeamStockKey,1);
    }

    @Override
    public void unLockUserLock(String userLockKey) {
         redisService.remove(userLockKey);
    }

    @Override
    @Transactional(timeout = 5000)
    public void unpaid2Refund(GroupBuyRefundAggregate groupBuyRefundAggregate) {
        TradeRefundOrderEntity tradeRefundOrderEntity = groupBuyRefundAggregate.getTradeRefundOrderEntity();
        GroupBuyProgressVO groupBuyProgressVO = groupBuyRefundAggregate.getGroupBuyProgressVO();

        // 更新用户订单明细
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(tradeRefundOrderEntity.getUserId());
        groupBuyOrderListReq.setOrderId(tradeRefundOrderEntity.getOrderId());

        int updateUnpaid2RefundCount = groupBuyOrderListDao.unpaid2Refund(groupBuyOrderListReq);
        if (1 != updateUnpaid2RefundCount) {
            log.error("逆向流程，更新订单状态(退单)失败 {} {}", tradeRefundOrderEntity.getUserId(), tradeRefundOrderEntity.getOrderId());
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }


        // 更新拼团订单
        GroupBuyOrder groupBuyOrderReq = new GroupBuyOrder();
        groupBuyOrderReq.setTeamId(tradeRefundOrderEntity.getTeamId());
        groupBuyOrderReq.setLockCount(groupBuyProgressVO.getLockCount());

        int updateTeamUnpaid2Refund = groupBuyOrderDao.unpaid2Refund(groupBuyOrderReq);
        if (1 != updateTeamUnpaid2Refund) {
            log.error("逆向流程，更新组队记录(退单)失败 {} {}", tradeRefundOrderEntity.getUserId(), tradeRefundOrderEntity.getOrderId());
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }

        // 如果团队锁单锁单数量为0，则取消删除该拼团
       GroupBuyOrder groupBuyOrder = groupBuyOrderDao.queryGroupBuyTeamByTeamId(tradeRefundOrderEntity.getTeamId());
        if (groupBuyOrder.getLockCount() == 0) {
            int cancelTeamCount = groupBuyOrderDao.cancelTeam(tradeRefundOrderEntity.getTeamId());
            if (1 != cancelTeamCount) {
                log.error("逆向流程，取消组队失败 {} {}", tradeRefundOrderEntity.getUserId(), tradeRefundOrderEntity.getOrderId());
//                throw new AppException(ResponseCode.UPDATE_ZERO);
            }
        }
        // todo 退单后，不仅要修改数据库，还要对redis recoveryCount 进行恢复，后续统一处理

    }

    @Override
    @Transactional(timeout = 5000)
    public NotifyTaskEntity paid2Refund(GroupBuyRefundAggregate groupBuyRefundAggregate) {
        GroupBuyProgressVO groupBuyProgressVO = groupBuyRefundAggregate.getGroupBuyProgressVO();
        TradeRefundOrderEntity tradeRefundOrderEntity = groupBuyRefundAggregate.getTradeRefundOrderEntity();

        // 1.更新用户明细 退单
        String userId = tradeRefundOrderEntity.getUserId();
        String orderId = tradeRefundOrderEntity.getOrderId();
        GroupBuyOrderList groupBuyOrderListReq = new GroupBuyOrderList();
        groupBuyOrderListReq.setUserId(userId);
        groupBuyOrderListReq.setOrderId(orderId);
        int updatePaid2RefundCount = groupBuyOrderListDao.paid2Refund(groupBuyOrderListReq);
        if (1 != updatePaid2RefundCount) {
            log.error("逆向流程，更新订单状态(退单)失败 {} {}", userId, orderId);
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }
        // 2.更新拼团记录 退单
        GroupBuyOrder groupBuyOrderReq = new GroupBuyOrder();
        groupBuyOrderReq.setTeamId(tradeRefundOrderEntity.getTeamId());
        groupBuyOrderReq.setLockCount(groupBuyProgressVO.getLockCount());
        groupBuyOrderReq.setCompleteCount(groupBuyProgressVO.getCompleteCount());
        int updateTeamPaid2Refund = groupBuyOrderDao.paid2Refund(groupBuyOrderReq);
        if (1 != updateTeamPaid2Refund) {
            log.error("逆向流程，更新组队记录(退单)失败 {} {}", userId, orderId);
            throw new AppException(ResponseCode.UPDATE_ZERO);
        }
        // 3.填写本地方法表
        NotifyTask notifyTask = new NotifyTask();
        notifyTask.setActivityId(tradeRefundOrderEntity.getActivityId());
        notifyTask.setTeamId(tradeRefundOrderEntity.getTeamId());
        notifyTask.setNotifyType(NotifyTypeEnumVO.MQ.getCode());
        notifyTask.setNotifyMQ(topic_team_refund);
        notifyTask.setNotifyCount(0);
        notifyTask.setNotifyStatus(0);


        notifyTask.setParameterJson(JSON.toJSONString(new HashMap<String, Object>() {{
            put("type", RefundTypeEnumVO.PAID_UNFORMED.getCode());
            put("userId", tradeRefundOrderEntity.getUserId());
            put("teamId", tradeRefundOrderEntity.getTeamId());
            put("orderId", tradeRefundOrderEntity.getOrderId());
            put("activityId", tradeRefundOrderEntity.getActivityId());
        }}));

        notifyTaskDao.insert(notifyTask);

        return NotifyTaskEntity.builder()
                .teamId(notifyTask.getTeamId())
                .notifyType(notifyTask.getNotifyType())
                .notifyMQ(notifyTask.getNotifyMQ())
                .notifyCount(notifyTask.getNotifyCount())
                .parameterJson(notifyTask.getParameterJson())
                .build();
    }
}
