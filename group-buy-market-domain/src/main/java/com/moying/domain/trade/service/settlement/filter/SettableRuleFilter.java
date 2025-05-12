package com.moying.domain.trade.service.settlement.filter;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.entity.GroupBuyTeamEntity;
import com.moying.domain.trade.model.entity.MarketPayOrderEntity;
import com.moying.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import com.moying.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import com.moying.types.design.framework.link.model2.handler.ILogicHandler;
import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: moying
 * @CreateTime: 2025-05-12
 * @Description: 可结算规则过滤；交易时间
 */

@Service
@Slf4j
public class SettableRuleFilter implements ILogicHandler<TradeSettlementRuleCommandEntity, TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter,
                                                     TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("结算规则过滤-有效时间校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());

        // 从上下文中获取拼团信息
        MarketPayOrderEntity marketPayOrderEntity = dynamicContext.getMarketPayOrderEntity();

        // 查询拼团对象
        GroupBuyTeamEntity groupBuyTeamEntity = tradeRepository.queryGroupBuyTeamByTeamId(marketPayOrderEntity.getTeamId());

        // 外部交易时间
        Date outTradeTime = requestParameter.getOutTradeTime();
        if(outTradeTime.before(groupBuyTeamEntity.getValidStartTime())||outTradeTime.after(groupBuyTeamEntity.getValidEndTime())){
            log.error("订单交易时间不在拼团有效时间范围内");
            throw new AppException(ResponseCode.E0106);
        }

        // 设置上下文
        dynamicContext.setGroupBuyTeamEntity(groupBuyTeamEntity);

        return next(requestParameter,dynamicContext);
    }
}
