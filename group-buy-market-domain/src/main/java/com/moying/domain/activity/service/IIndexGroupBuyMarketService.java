package com.moying.domain.activity.service;

import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import org.springframework.stereotype.Service;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */


public interface IIndexGroupBuyMarketService {

    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;

}
