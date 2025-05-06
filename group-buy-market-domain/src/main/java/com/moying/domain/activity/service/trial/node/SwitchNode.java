package com.moying.domain.activity.service.trial.node;

import com.alibaba.fastjson2.JSON;
import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import com.moying.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import com.moying.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import com.moying.types.design.framework.tree.StrategyHandler;
import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
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
public class SwitchNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private MarketNode marketNode;

    @Override
    public TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("拼团商品查询试算服务-SwitchNode userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));

        // 根据用户ID切量
        String userId = requestParameter.getUserId();

        // 判断是否降级
        if(activityRepository.downgradeSwitch()){
            log.info("拼团商品查询试算服务-SwitchNode 降级 userId:{} requestParameter:{}", requestParameter.getUserId(), JSON.toJSONString(requestParameter));
            throw new AppException(ResponseCode.E0003.getCode(), ResponseCode.E0003.getInfo());
        }
        // 切量范围判断
        if (!activityRepository.cutRange(userId)) {
            log.info("拼团活动切量拦截 {}", userId);
            throw new AppException(ResponseCode.E0004.getCode(), ResponseCode.E0004.getInfo());
        }

        return router( requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return marketNode;
    }
}
