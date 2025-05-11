package com.moying.domain.trade.adapter.repository;

import com.moying.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import com.moying.domain.trade.model.entity.GroupBuyActivityEntity;
import com.moying.domain.trade.model.entity.MarketPayOrderEntity;
import com.moying.domain.trade.model.valobj.GroupBuyProgressVO;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 交易仓储服务接口
 */

public interface ITradeRepository {

    /**
     * 查询支付订单
     * @param userId 用户ID
     * @param outTradeNo 支付订单号
     * @return 支付订单实体
     */
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);

    // 锁定支付订单
    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);

    // 查询团购进度
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    // 查询团购活动
    GroupBuyActivityEntity queryGroupBuyActivityByActivityId(Long activityId);

    // 查询用户参与活动的订单量
    Integer queryOrderCountByActivityId(Long activityId, String userId);
}
