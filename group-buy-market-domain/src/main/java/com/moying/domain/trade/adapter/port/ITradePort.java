package com.moying.domain.trade.adapter.port;

import com.moying.domain.trade.model.entity.NotifyTaskEntity;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */

public interface ITradePort {
    String groupBuyNotify(NotifyTaskEntity notifyTask);
}
