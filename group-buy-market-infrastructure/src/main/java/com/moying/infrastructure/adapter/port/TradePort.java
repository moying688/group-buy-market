package com.moying.infrastructure.adapter.port;

import com.moying.domain.trade.adapter.port.ITradePort;
import com.moying.domain.trade.model.entity.NotifyTaskEntity;
import com.moying.domain.trade.model.valobj.NotifyTypeEnumVO;
import com.moying.infrastructure.event.EventPublisher;
import com.moying.infrastructure.gateway.GroupBuyNotifyService;
import com.moying.infrastructure.redis.IRedisService;
import com.moying.types.enums.NotifyTaskHTTPEnumVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */


@Service
@Slf4j
public class TradePort implements ITradePort {

    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;

    @Resource
    private IRedisService redisService;

    @Resource
    private EventPublisher publisher;
//    @Value("${mq.producer.topic.team-success}")
//    private String topic;

    @Override
    public String groupBuyNotify(NotifyTaskEntity notifyTask) {
        RLock lock = redisService.getLock(notifyTask.lockKey());
        try{
            // 拼团服务端会被部署到多台应用服务器上，那么就会有很多任务一起执行。这个时候要进行抢占，避免被多次执行
            if(lock.tryLock(3,0, TimeUnit.SECONDS)){ // 3秒内获取到锁，执行任务
                try{
                    if(NotifyTypeEnumVO.HTTP.getCode().equals(notifyTask.getNotifyType())){
                        // 无效的notifyUrl 则直接返回成功
                        if (StringUtils.isBlank(notifyTask.getNotifyUrl()) || "暂无".equals(notifyTask.getNotifyUrl())) {
                            return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                        }
                        // todo 要是notifyUrl服务没有启动需要额外处理一下
                        return groupBuyNotifyService.groupBuyNotify(notifyTask.getNotifyUrl(), notifyTask.getParameterJson());
                    }
                    // 回调方式 MQ
                    if (NotifyTypeEnumVO.MQ.getCode().equals(notifyTask.getNotifyType())) {
                        publisher.publish(notifyTask.getNotifyMQ(), notifyTask.getParameterJson());
                        return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                    }
                }finally {
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }catch (Exception e) {
            Thread.currentThread().interrupt();
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }
    }
}
