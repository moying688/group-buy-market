package com.moying.domain.trade.service.settlement;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.aggregate.GroupBuyTeamSettlementAggregate;
import com.moying.domain.trade.model.entity.*;
import com.moying.domain.trade.service.ITradeSettlementOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */


@Service
@Slf4j
public class TradeSettlementOrderService implements ITradeSettlementOrderService {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    public TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) {
        log.info("拼团交易-支付订单结算:{} outTradeNo:{}", tradePaySuccessEntity.getUserId(), tradePaySuccessEntity.getOutTradeNo());
        // 1. 查询拼团信息
        MarketPayOrderEntity marketPayOrderEntity = tradeRepository.queryMarketPayOrderEntityByOutTradeNo
                (tradePaySuccessEntity.getUserId(), tradePaySuccessEntity.getOutTradeNo());

        if (null == marketPayOrderEntity) {
            log.info("不存在的外部交易单号或用户已退单，不需要做支付订单结算:{} outTradeNo:{}", tradePaySuccessEntity.getUserId(), tradePaySuccessEntity.getOutTradeNo());
            return null;
        }

        // 2.查询组团信息
        GroupBuyTeamEntity groupBuyTeamEntity = tradeRepository.queryGroupBuyTeamByTeamId(marketPayOrderEntity.getTeamId());

        // 3.构建聚合对象
        GroupBuyTeamSettlementAggregate groupBuyTeamSettlementAggregate = GroupBuyTeamSettlementAggregate.builder()
                .userEntity(UserEntity.builder().userId(tradePaySuccessEntity.getUserId()).build())
                .groupBuyTeamEntity(groupBuyTeamEntity)
                .tradePaySuccessEntity(tradePaySuccessEntity)
                .build();

        // 4. 拼团交易结算
        tradeRepository.settlementMarketPayOrder(groupBuyTeamSettlementAggregate);

        // 5.返回结算信息
        return TradePaySettlementEntity.builder()
                .source(tradePaySuccessEntity.getSource())
                .channel(tradePaySuccessEntity.getChannel())
                .userId(tradePaySuccessEntity.getUserId())
                .teamId(marketPayOrderEntity.getTeamId())
                .activityId(groupBuyTeamEntity.getActivityId())
                .outTradeNo(tradePaySuccessEntity.getOutTradeNo())
                .build();
    }
}
