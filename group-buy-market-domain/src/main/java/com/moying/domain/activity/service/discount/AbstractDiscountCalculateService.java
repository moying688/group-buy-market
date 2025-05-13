package com.moying.domain.activity.service.discount;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.domain.activity.model.valobj.DiscountTypeEnum;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.tags.adapter.repository.ITagRepository;
import com.moying.domain.tags.service.ITagService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: 折扣计算抽象类
 */


@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Resource
    private IActivityRepository activityRepository;
    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        // 1. 人群标签过滤
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowdRange = filterTagId(userId, groupBuyDiscount.getTagId());
            if (!isCrowdRange) {
                log.info("折扣优惠计算拦截，用户不再优惠人群标签范围内 userId:{}", userId);
                return originalPrice;
            }
        }

        // 2. 折扣优惠计算
        return doCalculate(originalPrice,groupBuyDiscount);
    }


    // 人群标签过滤 - 限定人群优惠
    private  boolean filterTagId(String userId,String tagId){
        activityRepository.isTagCrowdRange(tagId,userId);
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
