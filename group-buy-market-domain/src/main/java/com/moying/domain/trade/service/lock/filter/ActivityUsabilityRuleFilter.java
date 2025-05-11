package com.moying.domain.trade.service.lock.filter;

import com.moying.domain.trade.adapter.repository.ITradeRepository;
import com.moying.domain.trade.model.entity.GroupBuyActivityEntity;
import com.moying.domain.trade.model.entity.TradeRuleCommandEntity;
import com.moying.domain.trade.model.entity.TradeRuleFilterBackEntity;
import com.moying.domain.trade.service.lock.factory.TradeRuleFilterFactory;
import com.moying.types.design.framework.link.model2.handler.ILogicHandler;
import com.moying.types.enums.ActivityStatusEnumVO;
import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */


@Service
@Slf4j
public class ActivityUsabilityRuleFilter implements
        ILogicHandler<TradeRuleCommandEntity, TradeRuleFilterFactory.DynamicContext,TradeRuleFilterBackEntity> {
    @Resource
    private ITradeRepository tradeRepository;

    @Override
    public TradeRuleFilterBackEntity apply(TradeRuleCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        // 活动是否可参与（时间校验等）
        log.info("交易规则过滤-活动的可用性校验{} activityId:{}", requestParameter.getUserId(), requestParameter.getActivityId());

        // 查询拼团活动
        GroupBuyActivityEntity groupBuyActivity = tradeRepository.queryGroupBuyActivityByActivityId(requestParameter.getActivityId());


        // 校验；活动状态 - 可以抛业务异常code，或者把code写入到动态上下文dynamicContext中，最后获取。
        if (!ActivityStatusEnumVO.EFFECTIVE.equals(groupBuyActivity.getStatus())) {
            log.info("活动的可用性校验，非生效状态 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0101);
        }

        // 校验活动时间范围
        Date startTime = groupBuyActivity.getStartTime();
        Date endTime = groupBuyActivity.getEndTime();
        Date currTime = new Date();
        if(currTime.after(endTime)||currTime.before(startTime)){
            log.info("活动的可用性校验，非可参与时间范围 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0102);
        }

        dynamicContext.setGroupBuyActivity(groupBuyActivity);

        // 进入下一个责任链结点
        return next(requestParameter,dynamicContext);
    }
}
