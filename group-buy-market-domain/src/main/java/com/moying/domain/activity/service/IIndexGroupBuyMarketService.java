package com.moying.domain.activity.service;

import com.moying.domain.activity.model.entity.MarketProductEntity;
import com.moying.domain.activity.model.entity.TrialBalanceEntity;
import com.moying.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import com.moying.domain.activity.model.valobj.TeamStatisticVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description: 首页团购预算服务接口
 */


public interface IIndexGroupBuyMarketService {

    /**
     * 首页预算
     * @param marketProductEntity
     * @return
     * @throws Exception
     */
    TrialBalanceEntity indexMarketTrial(MarketProductEntity marketProductEntity) throws Exception;


    /**
     * 查询进行中的拼团订单
     *
     * @param activityId  活动ID
     * @param userId      用户ID
     * @param ownerCount  个人数量
     * @param randomCount 随机数量
     * @return 用户拼团明细数据
     */
    List<UserGroupBuyOrderDetailEntity> queryInProgressUserGroupBuyOrderDetailList(Long activityId, String userId, Integer ownerCount, Integer randomCount);

    /**
     * 活动拼团队伍总结
     *
     * @param activityId 活动ID
     * @return 队伍统计
     */
    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);
}
