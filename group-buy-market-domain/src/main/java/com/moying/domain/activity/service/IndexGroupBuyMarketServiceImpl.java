package com.moying.domain.activity.service;

import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import com.moying.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import com.moying.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: 首页
 */

@Service
public class IndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService {


    @Resource
    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler
                = defaultActivityStrategyFactory.strategyHandler();
        TrialBalanceEntity trialBalanceEntity = strategyHandler.apply(marketProductEntity, new DefaultActivityStrategyFactory.DynamicContext());
        return trialBalanceEntity;
    }
}
