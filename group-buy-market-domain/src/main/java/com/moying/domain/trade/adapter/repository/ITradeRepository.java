package com.moying.domain.trade.adapter.repository;

import com.moying.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.moying.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import com.moying.domain.trade.model.entity.GroupBuyActivityEntity;
import com.moying.domain.trade.model.entity.GroupBuyTeamEntity;
import com.moying.domain.trade.model.entity.MarketPayOrderEntity;
import com.moying.domain.trade.model.entity.NotifyTaskEntity;
import com.moying.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import com.moying.domain.trade.model.valobj.GroupBuyProgressVO;

import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 交易仓储服务接口
 */

public interface ITradeRepository {

    /**
     * 查询支付订单
     * @param userId 用户ID
     * @param outTradeNo 支付订单号
     * @return 支付订单实体
     */
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    /**
     * 团购订单支付
     * @param groupBuyOrderAggregate 团购订单聚合
     * @return 支付订单实体
     */
    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    /**
     * 查询团购进度
     * @param teamId 团购团队ID
     * @return 团购进度VO
     */
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    /**
     * 查询团购活动
     * @param activityId 团购活动ID
     * @return 团购活动实体
     */
    GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId);

    /**
     * 查询用户团购订单数量
     * @param activityId 团购活动ID
     * @param userId 用户ID
     * @return 团购订单数量
     */
    Integer queryOrderCountByActivityId(Long activityId, String userId);

    /**
     * 查询团购团队
     * @param teamId 团购团队ID
     * @return 团购团队实体
     */
    GroupBuyTeamEntity queryGroupBuyTeamByTeamId(String teamId);

    /**
     * 团购团队结算
     * @param groupBuyTeamSettlementAggregate 团购团队结算聚合
     * @return 通知任务实体
     */
    NotifyTaskEntity settlementMarketPayOrder(GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate);

    /**
     * 校验是否是SC黑名单
     * @param source 来源
     * @param channel 渠道
     * @return true: 是黑名单 false: 不是黑名单
     */
    boolean isSCBlackIntercept(String source, String channel);

    /**
     * 查询未执行的通知任务列表
     * @return 未执行的通知任务列表
     */
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList();

    /**
     * 查询未执行的通知任务列表
     * @param teamId 团购团队ID
     * @return 未执行的通知任务列表
     */
    List<NotifyTaskEntity> queryUnExecutedNotifyTaskList(String teamId);

    /**
     * 更新通知任务状态为成功
     * @param teamId 团购团队ID
     * @return 更新数量
     */
    int updateNotifyTaskStatusSuccess(String teamId);

    /**
     * 更新通知任务状态为失败
     * @param teamId 团购团队ID
     * @return 更新数量
     */
    int updateNotifyTaskStatusError(String teamId);

    /**
     * 更新通知任务状态为重试
     * @param teamId 团购团队ID
     * @return 更新数量
     */
    int updateNotifyTaskStatusRetry(String teamId);

    /**
     * 查询团购团队
     * @param userId 用户ID
     * @param teamId 团购团队ID
     * @return true: 是 false: 不是
     */

    boolean queryGroupBuyTeamByUserIdAndTeamId(String userId, String teamId);

    /**
     * 占用团购团队库存
     * @param teamStockKey 团购团队库存键
     * @param recoveryTeamStockKey 团购团队库存恢复键
     * @param userLockKey 用户锁键
     * @param target 目标数量
     * @param validTime 有效时间
     * @return true: 占用成功 false: 占用失败
     */
    boolean occupyTeamStock(String teamStockKey, String recoveryTeamStockKey,String userLockKey, Integer target, Integer validTime);

    /**
     * 团购团队库存恢复
     * @param recoveryTeamStockKey 团购团队库存恢复键
     * @param validTime 有效时间
     */
    void recoveryTeamStock(String recoveryTeamStockKey, Integer validTime);


    /**
     * 解锁用户锁
     * @param userLockKey 用户锁键
     */
    void unLockUserLock(String userLockKey);
    /**
     * 待退款订单聚合(未成团 未支付)
     * @param groupBuyRefundAggregate 待退款订单聚合
     */
    void unpaid2Refund(GroupBuyRefundAggregate groupBuyRefundAggregate);
}
