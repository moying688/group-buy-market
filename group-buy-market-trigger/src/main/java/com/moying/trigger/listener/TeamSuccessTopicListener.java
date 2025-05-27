package com.moying.trigger.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
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

@RocketMQMessageListener(topic = "group_team_success_topic",
        consumerGroup = "group_buy_team_success_consumer")
@Component
public class TeamSuccessTopicListener implements RocketMQListener<String>
//        , RocketMQPushConsumerLifecycleListener
{


    @Value("${server.port}")
    private String serverPort;
    @Override
    public void onMessage(String message) {
        System.out.println("接收到消息：" + message);
    }

//    @Override
//    public void prepareStart(DefaultMQPushConsumer consumer) {
//        // 获取当前进程端口号
//
//        consumer.setInstanceName("instance"+serverPort);
//    }

}
