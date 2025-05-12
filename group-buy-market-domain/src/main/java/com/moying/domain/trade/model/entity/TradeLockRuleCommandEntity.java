package com.moying.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description: 拼团锁单命令实体
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLockRuleCommandEntity {
    // 用户 id
    private String userId;
    // 活动id
    private Long activityId;
}
