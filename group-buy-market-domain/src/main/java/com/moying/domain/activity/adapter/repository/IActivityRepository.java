package com.moying.domain.activity.adapter.repository;

import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.model.valobj.SkuVO;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public interface IActivityRepository {

    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(String source,String channel);

    SkuVO querySkuByGoodsId(String goodsId);
}
