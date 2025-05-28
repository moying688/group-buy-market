package com.moying.infrastructure.adapter.repository;

import com.moying.infrastructure.dcc.DCCService;
import com.moying.infrastructure.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.function.Supplier;

/**
 * @Author: moying
 * @CreateTime: 2025-05-28
 * @Description: 仓储抽象类
 */

public class AbstractRepository {

    private final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    @Resource
    protected IRedisService redisService;

    @Resource
    protected DCCService dccService;

    protected <T> T getFromCacheOrDB(String cacheKey, Supplier<T> dbFallback){
        // 判断是否开启缓存
        if (dccService.isCacheOpenSwitch()) {
            // todo 后续可以增加缓存穿透、缓存雪崩等处理逻辑
            // 从缓存获取
            T cacheResult = redisService.getValue(cacheKey);
            // 缓存存在则直接返回
            if (null != cacheResult) {
                return cacheResult;
            }
            // 缓存不存在则从数据库获取
            T dbResult = dbFallback.get();
            // 数据库查询结果为空则直接返回
            if (null == dbResult) {
                return null;
            }
            // 写入缓存
            redisService.setValue(cacheKey, dbResult);
            return dbResult;
        } else {
            // 缓存未开启，直接从数据库获取
            logger.warn("缓存降级 {}", cacheKey);
            return dbFallback.get();
        }
    }

}
