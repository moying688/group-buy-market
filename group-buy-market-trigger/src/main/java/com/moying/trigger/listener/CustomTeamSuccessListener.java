package com.moying.trigger.listener;

//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//
//@Component
//@Slf4j
//public class CustomTeamSuccessListener implements RocketMQPushConsumerLifecycleListener {
//
//    @Override
//    public void prepareStart(DefaultMQPushConsumer consumer) {
//        consumer.setConsumerGroup("group_buy_team_success_consumer");
//        consumer.setNamesrvAddr("127.0.0.1:9876");
//        try {
//            consumer.subscribe("group_team_success_topic", "*");
//        } catch (MQClientException e) {
//            throw new RuntimeException(e);
//        }
//
//        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
//            for (MessageExt msg : msgs) {
//                try {
//                    String body = new String(msg.getBody(), StandardCharsets.UTF_8);
//                    log.info("拼团完成，接收消息原文：{}", body);
//
//                    // TODO: 你的业务逻辑
//
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                } catch (Exception e) {
//                    log.error("消费失败，消息ID：{}", msg.getMsgId(), e);
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//            }
//            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//        });
//    }
//}
