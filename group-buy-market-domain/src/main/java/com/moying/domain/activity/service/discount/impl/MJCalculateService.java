package com.moying.domain.activity.service.discount.impl;

import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.service.discount.AbstractDiscountCalculateService;
import com.moying.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: MJ折扣计算
 */


@Slf4j
@Service("MJ")
public class MJCalculateService  extends AbstractDiscountCalculateService {


    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {

        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());
        // 折扣表达式 - 100,10 - 满100 减10
        String marketExpr = groupBuyDiscount.getMarketExpr();
        String[] split = marketExpr.split(Constants.SPLIT);
        BigDecimal x = new BigDecimal(split[0]);
        BigDecimal y = new BigDecimal(split[1]);


        if(originalPrice.compareTo(x) < 0){
            return originalPrice;
        }

        BigDecimal deductionPrice = originalPrice.subtract(y);

        // 判断折扣后金额，最低支付0.01
        if(deductionPrice.compareTo(BigDecimal.ZERO) <= 0){
            return new BigDecimal("0.01");
        }
        return deductionPrice;
    }
}
