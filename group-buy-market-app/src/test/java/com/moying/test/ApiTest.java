package com.moying.test;

import apache.rocketmq.v2.MQAdmin;
import com.moying.infrastructure.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {


    @Resource
    private EventPublisher publisher;

    @Value("${mq.producer.topic.team-success}")
    private String topic;


    @Test
    public void test() {
        log.info("测试完成");
    }


    @Test
    public void test_rocketmq() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        publisher.publish(topic, "订单结算：ORD-20231234");
        publisher.publish(topic, "订单结算：ORD-20231235");
        publisher.publish(topic, "订单结算：ORD-20231236");
        publisher.publish(topic, "订单结算：ORD-20231237");
        publisher.publish(topic, "订单结算：ORD-20231238");


        // 等待，消息消费。测试后，可主动关闭。
        countDownLatch.await();
    }


}
