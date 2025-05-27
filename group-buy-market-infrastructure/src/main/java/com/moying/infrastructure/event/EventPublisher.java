package com.moying.infrastructure.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-27
 * @Description:
 */

@Slf4j
@Component
public class EventPublisher {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void publish(String topic,String message){
        try {
            log.info("Publishing message to topic: {}, message: {}", topic, message);
            rocketMQTemplate.convertAndSend(topic, message);
        }catch (Exception e){
            log.error("发送MQ消息失败 team_success message:{}", message, e);
            throw e;
        }
    }
}
