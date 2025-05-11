package com.moying.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRuleFilterBackEntity {
    // 用户参与活动的订单量
    private Integer userTakeOrderCount;
}
