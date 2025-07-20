package com.moying.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    @Value("${mq.consumer.group.team-refund}")
    private String test;
    @Override
    public void onMessage(String message) {
        log.info("接收消息（退单成功）:{}", message);
    }
}
