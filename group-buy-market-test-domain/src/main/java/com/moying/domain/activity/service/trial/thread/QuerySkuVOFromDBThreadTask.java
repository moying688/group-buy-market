package com.moying.domain.activity.service.trial.thread;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.domain.activity.model.valobj.SkuVO;

import java.util.concurrent.Callable;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public class QuerySkuVOFromDBThreadTask implements Callable<SkuVO> {



    private final String goodsId;
    private final IActivityRepository activityRepository;

    public QuerySkuVOFromDBThreadTask(String goodsId, IActivityRepository activityRepository) {
        this.goodsId = goodsId;
        this.activityRepository = activityRepository;
    }

    @Override
    public SkuVO call() throws Exception {
        return activityRepository.querySkuByGoodsId(goodsId);
    }
}
