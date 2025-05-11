package com.moying.types.design.framework.link.model1;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 责任链装配
 */

public interface ILogicChainArmory<T, D, R> {

    ILogicLink<T, D, R> next();

    ILogicLink<T, D, R> appendNext(ILogicLink<T, D, R> next);

}
