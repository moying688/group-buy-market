package com.moying.domain.activity.service.discount;

import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public interface IDiscountCalculateService {


    /**
     * 折扣计算
     * @param userId 用户id
     * @param originalPrice 原价
     * @param groupBuyDiscount 折扣计划配置
     * @return 商品优惠价格
     */
    BigDecimal calculate(String userId, BigDecimal originalPrice,
                         GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);
}
