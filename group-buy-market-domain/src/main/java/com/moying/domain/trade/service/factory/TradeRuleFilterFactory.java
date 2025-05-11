package com.moying.domain.trade.service.factory;

import com.moying.domain.trade.model.entity.GroupBuyActivityEntity;
import com.moying.domain.trade.model.entity.TradeRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeRuleFilterBackEntity;
import com.moying.domain.trade.service.filter.ActivityUsabilityRuleFilter;
import com.moying.domain.trade.service.filter.UserTakeLimitRuleFilter;
import com.moying.types.design.framework.link.model2.LinkArmory;
import com.moying.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */
@Slf4j
@Service
public class TradeRuleFilterFactory {



    @Bean("tradeRuleFilter")
    public BusinessLinkedList<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity>
        tradeRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter, UserTakeLimitRuleFilter userTakeLimitRuleFilter){
        // 组装链
        LinkArmory<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeRuleFilterBackEntity>
                linkArmory = new LinkArmory<>("交易规则过滤链", activityUsabilityRuleFilter, userTakeLimitRuleFilter);
        return linkArmory.getLogicLink();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DynamicContext{

        private GroupBuyActivityEntity groupBuyActivity;

    }
}
