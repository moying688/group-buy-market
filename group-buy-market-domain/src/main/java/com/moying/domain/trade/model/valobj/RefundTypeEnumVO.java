package com.moying.domain.trade.model.valobj;

import com.moying.types.enums.GroupBuyOrderEnumVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description: 退单类型枚举
 */


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum RefundTypeEnumVO {


    /**
     * 未支付，未成团
     */
    UNPAID_UNLOCK("unpaid_unlock", "unpaid2RefundStrategy", "未支付，未成团") {
        @Override
        public boolean matches(GroupBuyOrderEnumVO groupBuyOrderEnumVO, TradeOrderStatusEnumVO tradeOrderStatusEnumVO) {
            return GroupBuyOrderEnumVO.PROGRESS.equals(groupBuyOrderEnumVO) && TradeOrderStatusEnumVO.CREATE.equals(tradeOrderStatusEnumVO);
        }
    },

    /**
     * 已支付，未成团
     */
    PAID_UNFORMED("paid_unformed", "paid2RefundStrategy", "已支付，未成团") {
        @Override
        public boolean matches(GroupBuyOrderEnumVO groupBuyOrderEnumVO, TradeOrderStatusEnumVO tradeOrderStatusEnumVO) {
            return GroupBuyOrderEnumVO.PROGRESS.equals(groupBuyOrderEnumVO) && TradeOrderStatusEnumVO.COMPLETE.equals(tradeOrderStatusEnumVO);
        }
    },

    /**
     * 已支付，已成团
     */
    PAID_FORMED("paid_formed", "paidTeam2RefundStrategy", "已支付，已成团") {
        @Override
        public boolean matches(GroupBuyOrderEnumVO groupBuyOrderEnumVO, TradeOrderStatusEnumVO tradeOrderStatusEnumVO) {
            // 完成、完成含退单
            return (GroupBuyOrderEnumVO.COMPLETE.equals(groupBuyOrderEnumVO) || GroupBuyOrderEnumVO.COMPLETE_FAIL.equals(groupBuyOrderEnumVO))
                    && TradeOrderStatusEnumVO.COMPLETE.equals(tradeOrderStatusEnumVO);
        }
    },
    ;


    /**
     * 退款类型枚举
     */
    private String code;
    /**
     * 退款策略
     */
    private String strategy;
    /**
     * 退款类型描述
     */
    private String info;

    /**
     * 抽象方法，由每个枚举值实现自己的匹配逻辑
     */
    public abstract boolean matches(GroupBuyOrderEnumVO groupBuyOrderEnumVO, TradeOrderStatusEnumVO tradeOrderStatusEnumVO);


    /**
     * 根据状态组合获取对应的退款策略枚举
     * @param groupBuyOrderEnumVO 团购订单状态枚举
     * @param tradeOrderStatusEnumVO 交易订单状态枚举
     * @return 退款类型枚举
     */
    public static RefundTypeEnumVO getRefundStrategy(GroupBuyOrderEnumVO groupBuyOrderEnumVO, TradeOrderStatusEnumVO tradeOrderStatusEnumVO) {
        return Arrays.stream(values())
                .filter(refundType -> refundType.matches(groupBuyOrderEnumVO, tradeOrderStatusEnumVO))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("不支持的退款状态组合: groupBuyOrderStatus=" + groupBuyOrderEnumVO + ", tradeOrderStatus=" + tradeOrderStatusEnumVO));
    }


    /**
     * 根据枚举值获取退款类型枚举
     * @param code 枚举值
     * @return 退款类型枚举
     */
    public static RefundTypeEnumVO getRefundTypeEnumVOByCode(String code) {
        switch (code) {
            case "unpaid_unlock":
                return UNPAID_UNLOCK;
            case "paid_unformed":
                return PAID_UNFORMED;
            case "paid_formed":
                return PAID_FORMED;
            default:
                throw new RuntimeException("退单类型枚举值不存在: " + code);
        }
    }

}
