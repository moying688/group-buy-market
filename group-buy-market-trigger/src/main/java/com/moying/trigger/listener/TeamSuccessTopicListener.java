package com.moying.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Author: moying
 * @CreateTime: 2025-05-27
 * @Description:
 */
//mq:
//  producer:
//    topic:
//      team-success:
@RocketMQMessageListener(topic = "${mq.producer.topic.team-success}",
        consumerGroup = "${mq.consumer.group.team-success}",
        messageModel = MessageModel.BROADCASTING)
@Component
@Slf4j
public class TeamSuccessTopicListener implements RocketMQListener<String> {


    @Value("${server.port}")
    private String serverPort;

    @Value("${mq.producer.topic.team-success}")
    private String test;
    @Override
    public void onMessage(String message) {

        log.info("接收消息（组队成功）:{}", message);
    }

//    @Override
//    public void prepareStart(DefaultMQPushConsumer consumer) {
//        // 获取当前进程端口号
//
//        consumer.setInstanceName("instance"+serverPort);
//    }

}
