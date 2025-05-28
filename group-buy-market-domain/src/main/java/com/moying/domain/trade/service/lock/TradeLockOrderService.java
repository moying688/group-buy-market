package com.moying.domain.trade.service.lock;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.moying.domain.trade.model.entity.*;
import com.moying.domain.trade.model.valobj.GroupBuyProgressVO;
import com.moying.domain.trade.service.ITradeLockOrderService;
import com.moying.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import com.moying.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description:
 */


@Service
@Slf4j
public class TradeLockOrderService implements ITradeLockOrderService {

    @Resource
    private ITradeRepository tradeRepository;

    @Resource
    private BusinessLinkedList<TradeLockRuleCommandEntity,
            TradeLockRuleFilterFactory.DynamicContext, TradeLockRuleFilterBackEntity> tradeRuleFilter;

    @Override
    public MarketPayOrderEntity queryNoPayMarketPayOrderByOutTradeNo(String userId, String outTradeNo) {
        log.info("拼团交易-查询未支付营销订单:{} outTradeNo:{}", userId, outTradeNo);
        return tradeRepository.queryMarketPayOrderEntityByOutTradeNo(userId, outTradeNo);
    }

    @Override
    public GroupBuyProgressVO queryGroupBuyProgress(String teamId) {
        log.info("拼团交易-查询拼单进度:{}", teamId);
        return tradeRepository.queryGroupBuyProgress(teamId);
    }

    @Override
    public MarketPayOrderEntity lockMarketPayOrder(UserEntity userEntity, PayActivityEntity payActivityEntity, PayDiscountEntity payDiscountEntity) throws Exception {
        log.info("拼团交易-锁定营销优惠支付订单:{} activityId:{} goodsId:{}", userEntity.getUserId(), payActivityEntity.getActivityId(), payDiscountEntity.getGoodsId());

        // 校验用户是否还可以参与活动
        TradeLockRuleFilterBackEntity tradeLockRuleFilterBackEntity = tradeRuleFilter.apply(TradeLockRuleCommandEntity.builder()
                .activityId(payActivityEntity.getActivityId())
                .userId(userEntity.getUserId())
                .teamId(payActivityEntity.getTeamId())
                .build(), new TradeLockRuleFilterFactory.DynamicContext());


        // 构建聚合对象
        GroupBuyOrderAggregate groupBuyOrderAggregate = GroupBuyOrderAggregate.builder()
                .userEntity(userEntity)
                .payActivityEntity(payActivityEntity)
                .payDiscountEntity(payDiscountEntity)
                .userTakeOrderCount(tradeLockRuleFilterBackEntity.getUserTakeOrderCount())
                .build();

        // 锁定营销支付订单 - 这会用户只是下单还没有支付。后续会有2个流程；支付成功、超时未支付（回退）
       try{
           return tradeRepository.lockMarketPayOrder(groupBuyOrderAggregate);
       }catch (Exception e){
           // 记录失败恢复量
           tradeRepository.recoveryTeamStock(tradeLockRuleFilterBackEntity.getRecoveryTeamStockKey(), payActivityEntity.getValidTime());
           throw e;
        }
    }

    @Override
    public boolean queryGroupBuyTeamByUserIdAndTeamId(String userId, String teamId) {
        return tradeRepository.queryGroupBuyTeamByUserIdAndTeamId(userId, teamId);
    }
}
