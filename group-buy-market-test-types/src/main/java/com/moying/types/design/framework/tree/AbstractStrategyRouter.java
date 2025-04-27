package com.moying.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * 策略路由器
 * 用于路由策略
 * @param <T> 策略类型
 * @param <D> 策略数据
 * @param <R> 策略结果
 *
 * @author moying
 */
public abstract class AbstractStrategyRouter<T,D,R> implements StrategyMapper<T,D,R>, StrategyHandler<T,D,R> {


    @Getter
    @Setter
    protected  StrategyHandler<T,D,R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    /**
     * 执行
     * @param requestParameter 请求参数
     * @param dynamicContext 动态上下文
     * @return 策略结果
     * @throws Exception 异常
     *
     */
    public R router(T requestParameter, D dynamicContext) throws Exception {
        StrategyHandler<T, D, R> strategyHandler = get(requestParameter, dynamicContext);
        if(null != strategyHandler) return strategyHandler.apply(requestParameter, dynamicContext);
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
