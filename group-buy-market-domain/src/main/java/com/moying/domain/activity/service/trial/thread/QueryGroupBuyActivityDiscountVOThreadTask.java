package com.moying.domain.activity.service.trial.thread;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.model.valobj.SCSkuActivityVO;

import java.util.concurrent.Callable;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {


    /**
     * 来源
     */
    private final String source;

    /**
     * 渠道
     */
    private final String channel;

    /**
     * 商品ID
     */
    private final String goodsId;

    /**
     * 活动仓储
     */
    private final IActivityRepository activityRepository;

    // 通过构造函数注入依赖以及查询参数
    public QueryGroupBuyActivityDiscountVOThreadTask(String source, String channel,
                                                     String goodsId,IActivityRepository activityRepository) {
        this.source = source;
        this.channel = channel;
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        SCSkuActivityVO scSkuActivityVO = activityRepository.querySCSkuActivityBySCGoodsId(source, channel, goodsId);
        if(null == scSkuActivityVO) return null;
        return activityRepository.queryGroupBuyActivityDiscountVO(scSkuActivityVO.getActivityId());
    }
}
