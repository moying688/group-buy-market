package com.moying.domain.trade.model.entity;

import com.moying.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import com.moying.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: moying
 * @CreateTime: 2025-05-12
 * @Description: 交易结算命令
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeSettlementRuleCommandEntity {
    /** 用户ID */
    private String userId;
    /** 外部交易号 */
    private String outTradeNo;
    /** 渠道 */
    private String channel;
    /** 来源 */
    private String source;
    /** 外部交易时间 */
    private Date outTradeTime;

}
