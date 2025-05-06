package com.moying.domain.activity.adapter.repository;

import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.model.valobj.SCSkuActivityVO;
import com.moying.domain.activity.model.valobj.SkuVO;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: 活动仓储
 */

public interface IActivityRepository {

    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId);

    SkuVO querySkuByGoodsId(String goodsId);

    SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId);

    boolean isTagCrowdRange(String tagId, String userId);
}
