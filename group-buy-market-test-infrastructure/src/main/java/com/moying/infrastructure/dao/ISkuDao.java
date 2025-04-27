package com.moying.infrastructure.dao;

import com.moying.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */
@Mapper
public interface ISkuDao {

    Sku querySkuByGoodsId(String goodsId);

}
