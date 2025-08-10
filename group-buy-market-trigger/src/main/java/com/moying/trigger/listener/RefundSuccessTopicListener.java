package com.moying.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.moying.domain.trade.model.valobj.TeamRefundSuccess;
import com.moying.domain.trade.service.ITradeRefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-07-20
 * @Description: 退单接收
 */


@RocketMQMessageListener(topic = "${mq.producer.topic.team-refund}",
        consumerGroup = "${mq.consumer.group.team-refund}",
        messageModel = MessageModel.BROADCASTING)
@Component
@Slf4j
public class RefundSuccessTopicListener implements RocketMQListener<String> {

    @Value("${mq.producer.topic.team-refund}")
    private String test;

    @Resource
    private ITradeRefundOrderService tradeRefundOrderService;

    /**
     * 此流程具备最终一致性；
     * 1. 数据库锁单量恢复完成，本地消息表补偿MQ，确保MQ消息一定会发送。
     * 2. MQ 消息消费，恢复锁单量库存。库存时添加分布式锁，确保不会重复操作。
     * 3. MQ 消息重试，确保在失败情况下，可以重复消息，又因为有分布式锁的处理，可以确保重复消费也不会重复添加锁单量库粗。
     */
    @Override
    public void onMessage(String message) {
        log.info("接收消息（退单成功）- 恢复拼团队伍锁单量:{}", message);

        TeamRefundSuccess teamRefundSuccess = JSON.parseObject(message, TeamRefundSuccess.class);
        try{
            tradeRefundOrderService.restoreTeamLockStock(teamRefundSuccess);
        }catch (Exception e){
            log.info("接收消息（退单成功）- 恢复拼团队伍锁单量失败:{}", message, e);
            // 抛异常，mq消息会重试
            throw new RuntimeException(e);
        }
    }
}
