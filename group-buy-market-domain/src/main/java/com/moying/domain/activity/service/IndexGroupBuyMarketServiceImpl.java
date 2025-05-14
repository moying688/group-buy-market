package com.moying.domain.activity.service;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import com.moying.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import com.moying.domain.activity.model.valobj.TeamStatisticVO;
import com.moying.domain.activity.service.trial.factory.DefaultActivityStrategyFactory;
import com.moying.types.design.framework.tree.StrategyHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: 首页
 */

@Service
public class IndexGroupBuyMarketServiceImpl implements IIndexGroupBuyMarketService {


    @Resource
    private DefaultActivityStrategyFactory defaultActivityStrategyFactory;

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception {
        StrategyHandler<MarketProductEntity, DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler
                = defaultActivityStrategyFactory.strategyHandler();
        TrialBalanceEntity trialBalanceEntity = strategyHandler.apply(marketProductEntity,
                new DefaultActivityStrategyFactory.DynamicContext());
        return trialBalanceEntity;
    }

    @Override
    public List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailList(Long activityId, String userId, Integer ownerCount, Integer randomCount) {
       List<UserGroupBuyOrderDetailEntity>  unionAllList = new ArrayList<>();

       // 查询个人拼团数据
        if (0 != ownerCount) {
            List<UserGroupBuyOrderDetailEntity> ownerList = activityRepository.queryInProgressUserGroupBuyOrderDetailListByOwner(activityId, userId, ownerCount);
            if (null != ownerList && !ownerList.isEmpty()){
                unionAllList.addAll(ownerList);
            }
        }

        // 查询其他非个人拼团
        if (0 != randomCount) {
            List<UserGroupBuyOrderDetailEntity> randomList = activityRepository.queryInProgressUserGroupBuyOrderDetailListByRandom(activityId, userId, randomCount);
            if (null != randomList && !randomList.isEmpty()){
                unionAllList.addAll(randomList);
            }
        }

        return unionAllList;

    }

    @Override
    public TeamStatisticVO queryTeamStatisticByActivityId(Long activityId) {
        return activityRepository.queryTeamStatisticByActivityId(activityId);
    }
}
