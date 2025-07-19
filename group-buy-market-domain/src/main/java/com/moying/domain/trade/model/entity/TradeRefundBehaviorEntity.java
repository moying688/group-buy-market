package com.moying.domain.trade.model.entity;

import lombok.*;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description: 退款行为实体(退单完成后反馈给请求方)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRefundBehaviorEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 组队ID
     */
    private String teamId;

    /**
     * 行为枚举
     */
    private TradeRefundBehaviorEnum tradeRefundBehaviorEnum;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum TradeRefundBehaviorEnum {

        /**
         * 退款成功
         */
        SUCCESS("success", "成功"),

        /**
         * 退款重复
         */
        REPEAT("repeat", "重复"),

        /**
         * 退款失败
         */
        FAIL("fail", "失败"),

        ;

        private String code;
        private String info;
    }

}
