package com.moying.domain.trade.service;

import com.moying.domain.trade.model.entity.TradeRefundBehaviorEntity;
import com.moying.domain.trade.model.entity.TradeRefundCommandEntity;
import com.moying.domain.trade.model.valobj.TeamRefundSuccess;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description:
 */

public interface ITradeRefundOrderService {
    /**
     * 退款订单
     * @param tradeRefundCommandEntity 退款命令实体
     * @return 退款行为实体
     */
    TradeRefundBehaviorEntity refundOrder(TradeRefundCommandEntity tradeRefundCommandEntity);


    /**
     * 逆向流程，恢复锁单量
     * @param teamRefundSuccess 组队退款成功实体
     * @throws Exception 异常
     */
    void restoreTeamLockStock(TeamRefundSuccess teamRefundSuccess) throws Exception;
}
