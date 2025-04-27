package com.moying.types.design.framework.tree;


/**
 * 策略映射器
 * 用于映射策略
 * @author moying
 */
public interface StrategyMapper<T,D,R> {


    /**
     * * 获取策略处理器
     * @param requestParameter 请求参数
     * @param dynamicContext 动态上下文
     * @return 策略处理器
     */
    StrategyHandler<T,D,R> get(T requestParameter, D dynamicContext);


}
