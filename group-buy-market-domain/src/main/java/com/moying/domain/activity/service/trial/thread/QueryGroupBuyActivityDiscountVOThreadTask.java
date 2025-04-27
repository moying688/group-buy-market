package com.moying.domain.activity.service.trial.thread;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.util.concurrent.Callable;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public class QueryGroupBuyActivityDiscountVOThreadTask implements Callable<GroupBuyActivityDiscountVO> {


    private final String source;
    private final String channel;
    private final IActivityRepository activityRepository;

    // 通过构造函数注入依赖以及查询参数
    public QueryGroupBuyActivityDiscountVOThreadTask(String source, String channel, IActivityRepository activityRepository) {
        this.source = source;
        this.channel = channel;
        this.activityRepository = activityRepository;
    }

    @Override
    public GroupBuyActivityDiscountVO call() throws Exception {
        return activityRepository.queryGroupBuyActivityDiscountVO(source, channel);
    }
}
