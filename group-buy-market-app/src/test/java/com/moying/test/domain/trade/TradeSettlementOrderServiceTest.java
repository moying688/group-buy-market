package com.moying.test.domain.trade;

import com.alibaba.fastjson2.JSON;
import com.moying.domain.trade.model.entity.TradePaySettlementEntity;
import com.moying.domain.trade.model.entity.TradePaySuccessEntity;
import com.moying.domain.trade.service.ITradeSettlementOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeSettlementOrderServiceTest {

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    @Test
    public void test_settlementMarketPayOrder() throws Exception {
//        TradePaySuccessEntity tradePaySuccessEntity = new TradePaySuccessEntity();
//        tradePaySuccessEntity.setSource("s01");
//        tradePaySuccessEntity.setChannel("c01");
//        tradePaySuccessEntity.setUserId("moying01");
//        tradePaySuccessEntity.setOutTradeNo("407916072836");
//        tradePaySuccessEntity.setOutTradeTime(new Date());
//        TradePaySettlementEntity tradePaySettlementEntity = tradeSettlementOrderService.settlementMarketPayOrder(tradePaySuccessEntity);
//        log.info("请求参数:{}", JSON.toJSONString(tradePaySuccessEntity));
//        log.info("测试结果:{}", JSON.toJSONString(tradePaySettlementEntity));

        // 暂停，等待MQ消息。处理完后，手动关闭程序
        new CountDownLatch(1).await();
    }

}
