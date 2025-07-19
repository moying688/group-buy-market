package com.moying.domain.trade.service.refund.business.impl;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import com.moying.domain.trade.model.entity.TradeRefundOrderEntity;
import com.moying.domain.trade.service.refund.business.IRefundOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description: 待退款订单策略(未支付，未成团)
 */


@Slf4j
@Service("unpaid2RefundStrategy")
public class Unpaid2RefundStrategy implements IRefundOrderStrategy {

    @Resource
    private ITradeRepository tradeRepository;



    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) {
        log.info("退单；未支付，未成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(),
                tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        tradeRepository.unpaid2Refund(GroupBuyRefundAggregate.buildUnpaid2RefundAggregate(tradeRefundOrderEntity, -1));
    }
}
