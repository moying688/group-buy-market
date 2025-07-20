package com.moying.domain.trade.service.refund.business;

import com.moying.domain.trade.model.entity.TradeRefundOrderEntity;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description: 退款订单策略接口
 */
public interface IRefundOrderStrategy {

    /**
     * 退款订单
     * @param tradeRefundOrderEntity 退款订单实体
     */
    void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity);
}
