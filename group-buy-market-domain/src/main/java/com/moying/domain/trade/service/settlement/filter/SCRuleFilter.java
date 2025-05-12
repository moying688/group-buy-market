package com.moying.domain.trade.service.settlement.filter;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.entity.TradeSettlementRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeSettlementRuleFilterBackEntity;
import com.moying.domain.trade.service.settlement.factory.TradeSettlementRuleFilterFactory;
import com.moying.types.design.framework.link.model2.handler.ILogicHandler;
import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-12
 * @Description: 结算规则过滤-渠道黑名单校验
 */
@Slf4j
@Service
public class SCRuleFilter  implements ILogicHandler<TradeSettlementRuleCommandEntity,
        TradeSettlementRuleFilterFactory.DynamicContext, TradeSettlementRuleFilterBackEntity> {

    @Resource
    private ITradeRepository tradeRepository;

    @Override
    public TradeSettlementRuleFilterBackEntity apply(TradeSettlementRuleCommandEntity requestParameter,
                                                     TradeSettlementRuleFilterFactory.DynamicContext dynamicContext) throws Exception {

        log.info("结算规则过滤-渠道黑名单校验{} outTradeNo:{}", requestParameter.getUserId(), requestParameter.getOutTradeNo());


        // 渠道黑名单校验
       boolean intercept = tradeRepository.isSCBlackIntercept(requestParameter.getSource(),requestParameter.getChannel());

        if (intercept) {
            log.error("{}{} 渠道黑名单拦截", requestParameter.getSource(), requestParameter.getChannel());
            throw new AppException(ResponseCode.E0105);
        }

        return next(requestParameter, dynamicContext);
    }
}
