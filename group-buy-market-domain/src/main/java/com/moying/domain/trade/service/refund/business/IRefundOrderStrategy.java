package com.moying.domain.trade.service.refund.business;

import com.moying.domain.trade.model.entity.TradeRefundOrderEntity;
import com.moying.domain.trade.model.valobj.TeamRefundSuccess;

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


    /**
     * 逆向库存
     * @param teamRefundSuccess 拼团退单消息
     * @throws Exception 异常
     */
    void reverseStock(TeamRefundSuccess teamRefundSuccess) throws Exception;
}
