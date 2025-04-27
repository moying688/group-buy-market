package com.moying.domain.activity.service.discount;

import com.moying.domain.activity.model.valobj.DiscountTypeEnum;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;

import java.math.BigDecimal;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{
    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        // 1. 人群标签过滤
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowdRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if(!isCrowdRange) return originalPrice;
        }

        // 2. 折扣优惠计算
        return doCalculate(originalPrice,groupBuyDiscount);
    }


    // 人群标签过滤 - 限定人群优惠
    private  boolean filterTagId(String userId,String tagId){
        // todo 后续开发
        return true;
    }

    /**
     * 折扣计算
     * @param originalPrice 原价
     * @param groupBuyDiscount 折扣计划配置
     * @return 商品优惠价格
     */
    protected  abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);

}
