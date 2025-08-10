package com.moying.domain.trade.adapter.port;

import com.moying.domain.trade.model.entity.NotifyTaskEntity;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */

public interface ITradePort {
    /**
     * 团购订单通知
     * @param notifyTask 通知任务实体
     * @return 通知结果
     */
    String groupBuyNotify(NotifyTaskEntity notifyTask);
}
