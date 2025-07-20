package com.moying.domain.trade.model.aggregate;

import com.moying.domain.trade.model.entity.TradeRefundOrderEntity;
import com.moying.domain.trade.model.valobj.GroupBuyProgressVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupBuyRefundAggregate {


    /**
     * 退款订单实体
     */
    private TradeRefundOrderEntity tradeRefundOrderEntity;

    /**
     * 退单进度
     */
    private GroupBuyProgressVO groupBuyProgressVO;

    /**
     * 待退款订单聚合(未成团 未支付)
     * @param tradeRefundOrderEntity 退款订单实体
     * @param lockCount 锁数量
     * @return 待退款订单聚合
     */
    public static GroupBuyRefundAggregate buildUnpaid2RefundAggregate(TradeRefundOrderEntity tradeRefundOrderEntity, Integer lockCount) {
        GroupBuyRefundAggregate groupBuyRefundAggregate = new GroupBuyRefundAggregate();
        groupBuyRefundAggregate.setTradeRefundOrderEntity(tradeRefundOrderEntity);
        groupBuyRefundAggregate.setGroupBuyProgressVO(
                GroupBuyProgressVO.builder()
                        .lockCount(lockCount)
                        .build());
        return groupBuyRefundAggregate;
    }

    public static GroupBuyRefundAggregate buildPaid2RefundAggregate(TradeRefundOrderEntity tradeRefundOrderEntity,
                                                                    Integer lockCount,
                                                                    Integer completeCount){
        GroupBuyRefundAggregate groupBuyRefundAggregate = new GroupBuyRefundAggregate();
        groupBuyRefundAggregate.setTradeRefundOrderEntity(tradeRefundOrderEntity);
        groupBuyRefundAggregate.setGroupBuyProgressVO(
                GroupBuyProgressVO.builder()
                        .lockCount(lockCount)
                        .completeCount(completeCount)
                        .build());
        return groupBuyRefundAggregate;
    }

}
