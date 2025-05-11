package com.moying.types.design.framework.link.model2.chain;


import com.moying.types.design.framework.link.model2.handler.ILogicHandler;

/**
 * @description 业务链路
 */
public class BusinessLinkedList<T, D, R> extends
        LinkedList<ILogicHandler<T, D, R>> implements ILogicHandler<T, D, R>{

    public BusinessLinkedList(String name) {
        super(name);
    }

    @Override
    public R apply(T requestParameter, D dynamicContext) throws Exception {
        Node<ILogicHandler<T, D, R>> current = this.first;
        do {
            ILogicHandler<T, D, R> item = current.item;
            R apply = item.apply(requestParameter, dynamicContext);
            // 如果当前节点的处理器返回了结果，则直接返回
            // 返回null则会进入下一个结点
            if (null != apply) return apply;

            current = current.next;
        } while (null != current);

        return null;
    }

}
