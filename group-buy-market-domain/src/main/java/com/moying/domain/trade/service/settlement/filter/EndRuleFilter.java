package com.moying.domain.trade.service.settlement.filter;

import com.moying.domain.trade.model.entity.GroupBuyTeamEntity;
import com.moying.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import com.moying.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import com.moying.types.design.framework.link.model2.handler.ILogicHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: moying
 * @CreateTime: 2025-05-12
 * @Description:
 */

@Service
@Slf4j
public class EndRuleFilter
        implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {
    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter, TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("结算规则过滤-结束节点{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // 拼团组队实体对象
        GroupBuyTeamEntity groupBuyTeamEntity = dynamicContext.getGroupBuyTeamEntity();
        return TradeSettlementRuleFilterBackEntity.builder()
                .teamId(groupBuyTeamEntity.getTeamId())
                .activityId(groupBuyTeamEntity.getActivityId())
                .targetCount(groupBuyTeamEntity.getTargetCount())
                .completeCount(groupBuyTeamEntity.getCompleteCount())
                .lockCount(groupBuyTeamEntity.getLockCount())
                .status(groupBuyTeamEntity.getStatus())
                .validStartTime(groupBuyTeamEntity.getValidStartTime())
                .validEndTime(groupBuyTeamEntity.getValidEndTime())
                .notifyUrl(groupBuyTeamEntity.getNotifyUrl())
                .build();
    }
}
