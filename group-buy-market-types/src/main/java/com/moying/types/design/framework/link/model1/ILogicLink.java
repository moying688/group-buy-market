package com.moying.types.design.framework.link.model1;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 规则责任链接口
 */

public interface ILogicLink<T, D, R>
        extends ILogicChainArmory<T, D, R> {

    R apply(T requestParameter, D dynamicContext) throws  Exception;
}
