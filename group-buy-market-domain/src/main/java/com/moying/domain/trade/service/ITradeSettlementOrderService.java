package com.moying.domain.trade.service;

import com.moying.domain.trade.model.entity.NotifyTaskEntity;
import com.moying.domain.trade.model.entity.TradePaySettlementEntity;
import com.moying.domain.trade.model.entity.TradePaySuccessEntity;

import java.util.Map;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description: 拼团交易结算服务接口
 */

public interface ITradeSettlementOrderService {

    /**
     * 营销结算
     * @param tradePaySuccessEntity 交易支付订单实体对象
     * @return 交易结算订单实体
     */
    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception;


}
