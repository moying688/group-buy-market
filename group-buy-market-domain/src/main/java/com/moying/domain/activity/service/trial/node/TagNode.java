package com.moying.domain.activity.service.trial.node;

import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.service.trial.AbstractGroupBuyMarketSupport;
import com.moying.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import com.moying.domain.tags.service.TagService;
import com.moying.types.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-06
 * @Description:
 */
@Service
@Slf4j
public class TagNode extends AbstractGroupBuyMarketSupport<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> {

    @Resource
    private EndNode endNode;

    @Override
    protected TrialBalanceEntity doApply(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) throws Exception {
        GroupBuyActivityDiscountVO groupBuyActivityDiscountVO = dynamicContext.getGroupBuyActivityDiscountVO();
        String tagId = groupBuyActivityDiscountVO.getTagId();

        boolean isVisible = groupBuyActivityDiscountVO.isVisible();
        boolean isEnable = groupBuyActivityDiscountVO.isEnable();
        // 人群标签配置为空，则走默认值
        if(StringUtils.isBlank(tagId)){
            dynamicContext.setVisible(true);
            dynamicContext.setEnable(true);
            return router(requestParameter, dynamicContext);
        }

        // 是否在人群范围内；visible、enable 如果值为 ture 则表示没有配置拼团限制，那么就直接保证为 true 即可
       boolean isWithin = activityRepository.isTagCrowdRange(tagId,requestParameter.getUserId());
        dynamicContext.setVisible(isVisible || isWithin);
        dynamicContext.setEnable(isEnable || isWithin);
        return router(requestParameter, dynamicContext);
    }

    @Override
    public StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> get(MarketProductEntity requestParameter, DefaultActivityStrategyFactory.DynamicContext dynamicContext) {
        return endNode;
    }
}
