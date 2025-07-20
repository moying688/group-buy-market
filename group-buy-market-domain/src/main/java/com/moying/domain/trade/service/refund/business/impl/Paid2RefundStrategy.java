package com.moying.domain.trade.service.refund.business.impl;

import com.alibaba.fastjson.JSON;
import com.moying.domain.trade.adapter.port.ITradePort;
import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.aggregate.GroupBuyRefundAggregate;
import com.moying.domain.trade.model.entity.NotifyTaskEntity;
import com.moying.domain.trade.model.entity.TradeRefundOrderEntity;
import com.moying.domain.trade.service.ITradeTaskService;
import com.moying.domain.trade.service.refund.business.IRefundOrderStrategy;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 发起退单（未成团&已支付），锁单量-1、完成量-1、组队订单状态更新、发送退单消息（MQ）
 * @author moying
 * @CreateTime: 2025-07-19
 *
 */
@Slf4j
@Service("paid2RefundStrategy")
public class Paid2RefundStrategy implements IRefundOrderStrategy {


    @Resource
    private ITradeRepository tradeRepository;
    @Resource
    private ITradeTaskService tradeTaskService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void refundOrder(TradeRefundOrderEntity tradeRefundOrderEntity) {
        log.info("退单；已支付，未成团 userId:{} teamId:{} orderId:{}", tradeRefundOrderEntity.getUserId(), tradeRefundOrderEntity.getTeamId(), tradeRefundOrderEntity.getOrderId());

        // 1. 退单 已支付&未成团
        NotifyTaskEntity notifyTaskEntity = tradeRepository.
                paid2Refund(GroupBuyRefundAggregate.buildPaid2RefundAggregate(tradeRefundOrderEntity, -1, -1));

        // 2. 发送MQ消息
        if(null!=notifyTaskEntity){
            threadPoolExecutor.execute(() -> {
               Map<String,Integer> notifyResultMap = null;
                try {
                    notifyResultMap = tradeTaskService.execNotifyJob(notifyTaskEntity);
                    log.info("回调通知交易退单 result:{}", JSON.toJSONString(notifyResultMap));
                } catch (Exception e) {
                    log.error("回调通知交易退单失败 result:{}", JSON.toJSONString(notifyResultMap), e);
                    throw new AppException(e.getMessage());
                }
            });
        }
    }


}
