package com.moying.types.design.framework.tree;


/**
 * 策略处理器
 * 用于处理策略
 * @param <T> 策略类型
 * @param <D> 策略数据
 * @param <R> 策略结果
 *
 * @author moying
 */
public interface StrategyHandler<T,D,R> {


    StrategyHandler DEFAULT = (T,D) -> null;


    /**
     * 执行
     * @param requestParameter 请求参数
     * @param dynamicContext 动态上下文
     * @return 策略结果
     * @throws Exception 异常
     */
    R apply(T requestParameter, D dynamicContext) throws Exception;
}
