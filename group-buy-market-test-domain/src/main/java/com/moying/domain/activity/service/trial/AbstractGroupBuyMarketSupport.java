package com.moying.domain.activity.service.trial;

import com.moying.types.design.framework.tree.AbstractStrategyRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */


@Service
@Slf4j
public abstract class AbstractGroupBuyMarketSupport<MarketProductEntity,DynamicContext,TrialBalanceEntity > extends AbstractStrategyRouter<MarketProductEntity,DynamicContext,TrialBalanceEntity > {

}
