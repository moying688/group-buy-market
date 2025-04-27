package com.moying.domain.activity.service.trial;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.types.design.framework.tree.AbstractMultiThreadStrategyRouter;
import com.moying.types.design.framework.tree.AbstractStrategyRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */


@Service
@Slf4j
public abstract class AbstractGroupBuyMarketSupport<MarketProductEntity,DynamicContext,TrialBalanceEntity >
        extends AbstractMultiThreadStrategyRouter<MarketProductEntity,DynamicContext,TrialBalanceEntity > {


    protected  long timeout = 50;
    @Resource
    protected IActivityRepository activityRepository;
    @Override
    protected void multiThread(MarketProductEntity requestParameter, DynamicContext dynamicContext) throws Exception{

    }
}
