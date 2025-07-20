package com.moying.trigger.job;

import com.alibaba.fastjson.JSON;
import com.moying.domain.trade.service.ITradeSettlementOrderService;
import com.moying.domain.trade.service.ITradeTaskService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */

@Slf4j
@Service
public class GroupBuyNotifyJob {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ITradeTaskService tradeTaskService;

    @Scheduled(cron = "0 0 0 * * ?") // 每天0点执行
    public void exec(){

        // 多个实例都会执行该任务，防止并发问题，使用分布式锁
        RLock lock = redissonClient.getLock("group_buy_market_notify_job_exec");
        try {
            // waitTime：等待获取锁的最长时间
            // leaseTime：租约时间，如果当前线程成功获取到锁，那么锁将被持有的时间长度。
            // 这个时间过后，锁会自动释放。续租时间可按照执行方法时间的耗时max来设置。如 50毫秒
            boolean isLocked = lock.tryLock(3, 0, TimeUnit.SECONDS);
            // 未获取到锁，直接返回
            if (!isLocked) return;
            Map<String, Integer> result = tradeTaskService.execNotifyJob();
            log.info("定时任务，回调通知拼团完结任务 result:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("定时任务，回调通知拼团完结任务失败", e);
        }finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
