package com.moying.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public abstract class AbstractMultiThreadStrategyRouter<T, D, R>
        implements StrategyMapper<T, D, R>, StrategyHandler<T, D, R> {


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
    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {
        // 异步加载数据
        multiThread(requestParameter,dynamicContext);
        // 业务流程处理
        return doApply(requestParameter, dynamicContext);
    }


    protected abstract void multiThread(T requestParameter, D dynamicContext)  throws Exception;

    protected abstract R doApply(T requestParameter, D dynamicContext) throws Exception;



}
