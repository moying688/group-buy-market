package com.moying.api;

import com.moying.api.dto.GoodsMarketRequestDTO;
import com.moying.api.dto.GoodsMarketResponseDTO;
import com.moying.api.response.Response;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */



public interface IMarketIndexService {

    Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(GoodsMarketRequestDTO goodsMarketRequestDTO);
}
