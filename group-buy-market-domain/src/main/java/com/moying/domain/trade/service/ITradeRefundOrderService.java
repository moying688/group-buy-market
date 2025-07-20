package com.moying.domain.trade.service;

import com.moying.domain.trade.model.entity.TradeRefundBehaviorEntity;
import com.moying.domain.trade.model.entity.TradeRefundCommandEntity;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description:
 */

public interface ITradeRefundOrderService {


    TradeRefundBehaviorEntity refundOrder(TradeRefundCommandEntity tradeRefundCommandEntity);
}
