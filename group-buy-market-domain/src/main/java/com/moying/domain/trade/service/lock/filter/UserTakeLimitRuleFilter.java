package com.moying.domain.trade.service.lock.filter;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.entity.GroupBuyActivityEntity;
import com.moying.domain.trade.model.entity.TradeLockRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeLockRuleFilterBackEntity;
import com.moying.domain.trade.service.lock.factory.TradeLockRuleFilterFactory;
import com.moying.types.design.framework.link.model2.handler.ILogicHandler;
import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */


@Service
@Slf4j
public class UserTakeLimitRuleFilter  implements
        ILogicHandler<TradeLockRuleCommandEntity, TradeLockRuleFilterFactory.DynamicContext, TradeLockRuleFilterBackEntity> {

    @Resource
    private ITradeRepository tradeRepository;


    @Override
    public TradeLockRuleFilterBackEntity apply(TradeLockRuleCommandEntity requestParameter, TradeLockRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        // 用户参与次数校验等
        log.info("交易规则过滤-用户参与次数校验{} activityId:{}", requestParameter.getUserId(), requestParameter.getActivityId());

       GroupBuyActivityEntity groupBuyActivity =  dynamicContext.getGroupBuyActivity();

       // 查询用户在上一个拼团活动的参与次数
        Integer userTakeCount = tradeRepository.queryOrderCountByActivityId(requestParameter.getActivityId(),requestParameter.getUserId());
        // 校验用户参与次数
        if(null != groupBuyActivity.getTakeLimitCount() && userTakeCount >= groupBuyActivity.getTakeLimitCount()){
            log.info("用户参与次数校验，超过参与次数 activityId:{},userId:{}", requestParameter.getActivityId(),requestParameter.getUserId());
            throw new AppException(ResponseCode.E0103);
        }
        dynamicContext.setUserTakeOrderCount(userTakeCount);

        // 进入下一个责任结点
        return next(requestParameter, dynamicContext);

//        return TradeLockRuleFilterBackEntity.builder()
//                .userTakeOrderCount(userTakeCount)
//                .build();
    }
}
