package com.moying.domain.activity.service.discount.impl;

import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */

@Slf4j
@Service("N")
public class NCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        log.info("优惠策略折扣计算:{}", groupBuyDiscount.getDiscountType().getCode());
        String NPrice = groupBuyDiscount.getMarketExpr();
        BigDecimal bigDecimal = new BigDecimal(NPrice);
        if(bigDecimal.compareTo(BigDecimal.ZERO) <= 0){
            return new BigDecimal("0.01"); // 最低支付0.01
        }
        return bigDecimal;
    }
}
