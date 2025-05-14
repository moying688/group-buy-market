package com.moying.infrastructure.dao;

import com.moying.infrastructure.dao.po.SCSkuActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: moying
 * @CreateTime: 2025-05-05
 * @Description:
 */

@Mapper
public interface ISCSkuActivityDao {
    /**
     * 根据商品ID查询活动信息
     *
     * @param scSkuActivity
     * @return SCSkuActivity
     */
    SCSkuActivity querySCSkuActivityBySCGoodsId(SCSkuActivity scSkuActivity);
}
