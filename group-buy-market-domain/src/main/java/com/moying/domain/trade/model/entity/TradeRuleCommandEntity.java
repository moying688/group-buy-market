package com.moying.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRuleCommandEntity {
    // 用户 id
    private String userId;
    // 活动id
    private Long activityId;
}
