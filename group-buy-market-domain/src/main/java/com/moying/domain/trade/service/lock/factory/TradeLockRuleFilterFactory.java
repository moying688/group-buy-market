package com.moying.domain.trade.service.lock.factory;

import cn.bugstack.wrench.design.framework.link.model2.LinkArmory;
import cn.bugstack.wrench.design.framework.link.model2.chain.BusinessLinkedList;
import com.moying.domain.trade.model.entity.GroupBuyActivityEntity;
import com.moying.domain.trade.model.entity.TradeLockRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeLockRuleFilterBackEntity;
import com.moying.domain.trade.service.lock.filter.ActivityUsabilityRuleFilter;
import com.moying.domain.trade.service.lock.filter.TeamStockOccupyRuleFilter;
import com.moying.domain.trade.service.lock.filter.UserTakeLimitRuleFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */
@Slf4j
@Service
public class TradeLockRuleFilterFactory {


    private static final String teamStockKey = "group_buy_market_team_stock_key_";
    @Bean("tradeRuleFilter")
    public BusinessLinkedList<TradeLockRuleCommandEntity, DynamicContext, TradeLockRuleFilterBackEntity>
    tradeRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter, UserTakeLimitRuleFilter userTakeLimitRuleFilter
            , TeamStockOccupyRuleFilter teamStockOccupyRuleFilter) {
        // 组装链
        LinkArmory<TradeLockRuleCommandEntity, DynamicContext, TradeLockRuleFilterBackEntity>
                linkArmory = new LinkArmory<>("交易规则过滤链", activityUsabilityRuleFilter, userTakeLimitRuleFilter, teamStockOccupyRuleFilter);
        return linkArmory.getLogicLink();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DynamicContext {

//        private String teamStockKey = "group_buy_market_team_stock_key_";

        private GroupBuyActivityEntity groupBuyActivity;

        private Integer userTakeOrderCount;

        public String generateTeamStockKey(String teamId) {
            if (StringUtils.isBlank(teamId)) return null;
            return TradeLockRuleFilterFactory.generateTeamStockKey(groupBuyActivity.getActivityId(), teamId);
        }

        public String generateRecoveryTeamStockKey(String teamId) {
            if (StringUtils.isBlank(teamId)) return null;
            return TradeLockRuleFilterFactory.generateRecoveryTeamStockKey(groupBuyActivity.getActivityId(), teamId);
        }
//        public String generateUserLockKey(String userId, String teamId) {
//            if (StringUtils.isBlank(userId)) return null;
//            return "group_buy_market_user_lock_key_" + groupBuyActivity.getActivityId() + "_" + teamId + "_" + userId;
//        }
    }
    public static String generateTeamStockKey(Long activityId, String teamId){
        return teamStockKey + activityId + "_" + teamId;
    }

    public static String generateRecoveryTeamStockKey(Long activityId, String teamId) {
        return teamStockKey + activityId + "_" + teamId + "_recovery";
    }

    public static String generateUserLockKey(Long activityId, String teamId, String userId) {
        return "group_buy_market_user_lock_key_" + activityId + "_" + teamId + "_" + userId;
    }
}
