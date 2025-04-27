package com.moying.domain.activity.model.entity;

import lombok.*;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: 营销商品实体
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketProductEntity {

    /** 用户ID */
    private String userId;
    /** 商品ID */
    private String goodsId;
    /** 渠道 */
    private String source;
    /** 来源 */
    private String channel;
}
