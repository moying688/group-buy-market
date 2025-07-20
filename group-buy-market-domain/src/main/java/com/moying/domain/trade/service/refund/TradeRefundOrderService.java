package com.moying.domain.trade.service.refund;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.entity.*;
import com.moying.domain.trade.model.valobj.RefundTypeEnumVO;
import com.moying.domain.trade.model.valobj.TradeOrderStatusEnumVO;
import com.moying.domain.trade.service.ITradeRefundOrderService;
import com.moying.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import com.moying.domain.trade.service.refund.business.IRefundOrderStrategy;
import com.moying.types.enums.GroupBuyOrderEnumVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: moying
 * @CreateTime: 2025-07-19
 * @Description:
 */


@Service
@Slf4j
public class TradeRefundOrderService implements ITradeRefundOrderService {

    private final ITradeRepository tradeRepository;

    private final Map<String, IRefundOrderStrategy> refundOrderStrategyMap;

    public TradeRefundOrderService(ITradeRepository repository, Map<String, IRefundOrderStrategy> refundOrderStrategyMap) {
        this.tradeRepository = repository;
        this.refundOrderStrategyMap = refundOrderStrategyMap;
    }

    @Override
    public TradeRefundBehaviorEntity refundOrder(TradeRefundCommandEntity tradeRefundCommandEntity) {
        log.info("逆向流程，退单操作 userId:{} outTradeNo:{}", tradeRefundCommandEntity.getUserId(), tradeRefundCommandEntity.getOutTradeNo());

        // 1.查询外部交易订单，组队id、orderId、拼团状态
        MarketPayOrderEntity marketPayOrderEntity = tradeRepository
                .queryMarketPayOrderEntityByOutTradeNo(tradeRefundCommandEntity.getUserId(), tradeRefundCommandEntity.getOutTradeNo());
        TradeOrderStatusEnumVO tradeOrderStatusEnumVO = marketPayOrderEntity.getTradeOrderStatusEnumVO();
        String teamId = marketPayOrderEntity.getTeamId();
        String orderId = marketPayOrderEntity.getOrderId();

        // 返回幂等,已 完成退单
        if (TradeOrderStatusEnumVO.CLOSE.equals(tradeOrderStatusEnumVO)) {
            log.info("逆向流程，退单操作(幂等-重复退单) userId:{} outTradeNo:{}", tradeRefundCommandEntity.getUserId(), tradeRefundCommandEntity.getOutTradeNo());
            return TradeRefundBehaviorEntity.builder()
                    .userId(tradeRefundCommandEntity.getUserId())
                    .orderId(orderId)
                    .teamId(teamId)
                    .tradeRefundBehaviorEnum(TradeRefundBehaviorEntity.TradeRefundBehaviorEnum.REPEAT)
                    .build();
        }

        // 2. 查询拼团状态
        GroupBuyTeamEntity groupBuyTeamEntity = tradeRepository.queryGroupBuyTeamByTeamId(teamId);
        GroupBuyOrderEnumVO groupBuyOrderEnumVO = groupBuyTeamEntity.getStatus();

        // 3. 状态类型判断 - 使用策略模式获取退款类型
        RefundTypeEnumVO refundType = RefundTypeEnumVO.getRefundStrategy(groupBuyOrderEnumVO, tradeOrderStatusEnumVO);
        IRefundOrderStrategy refundOrderStrategy = refundOrderStrategyMap.get(refundType.getStrategy());
        refundOrderStrategy.refundOrder(TradeRefundOrderEntity.builder()
                .userId(tradeRefundCommandEntity.getUserId())
                .orderId(orderId)
                .teamId(teamId)
                .activityId(groupBuyTeamEntity.getActivityId())
                .build());

        // todo 对redis库存进行恢复
        // 通过策略模式，对三种情况分开讨论，已经成团的就不需要恢复
        TradeLockRuleFilterFactory.DynamicContext dynamicContext = new TradeLockRuleFilterFactory.DynamicContext();
        tradeRepository.recoveryTeamStock(dynamicContext.generateRecoveryTeamStockKey(teamId),60);

        return TradeRefundBehaviorEntity.builder()
                .userId(tradeRefundCommandEntity.getUserId())
                .orderId(orderId)
                .teamId(teamId)
                .tradeRefundBehaviorEnum(TradeRefundBehaviorEntity.TradeRefundBehaviorEnum.SUCCESS)
                .build();
    }
}
