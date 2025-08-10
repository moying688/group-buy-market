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

    /**
     * 预购订单
     * @param lockMarketPayOrderRequestDTO 预购订单请求DTO
     * @return 预购订单响应DTO
     */
    Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO);

    /**
     * 结算订单
     * @param requestDTO 结算订单请求DTO
     * @return 结算订单响应DTO
     */
    Response<SettlementMarketPayOrderResponseDTO> settlementMarketPayOrder(SettlementMarketPayOrderRequestDTO requestDTO);
}
