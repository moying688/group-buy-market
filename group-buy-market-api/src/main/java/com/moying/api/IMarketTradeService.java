package com.moying.api;

import com.moying.api.dto.LockMarketPayOrderRequestDTO;
import com.moying.api.dto.LockMarketPayOrderResponseDTO;
import com.moying.api.dto.SettlementMarketPayOrderRequestDTO;
import com.moying.api.dto.SettlementMarketPayOrderResponseDTO;
import com.moying.api.response.Response;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 营销交易服务接口
 */

public interface IMarketTradeService {
    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

    Response<SettlementMarketPayOrderResponseDTO> settlementMarketPayOrder(SettlementMarketPayOrderRequestDTO requestDTO);
}
