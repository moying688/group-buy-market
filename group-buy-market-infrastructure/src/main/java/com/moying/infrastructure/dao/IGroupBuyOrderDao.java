package com.moying.infrastructure.dao;

import com.moying.infrastructure.dao.po.GroupBuyOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description:
 */


@Mapper
public interface IGroupBuyOrderDao {


    /**
     * 订单入库
     * @param groupBuyOrder 订单
     */
    void insert(GroupBuyOrder groupBuyOrder);

    /**
     * 锁单成功
     * @param teamId 团购团队ID
     * @return 订单完成数量
     */
    int updateAddLockCount(String teamId);

    /**
     * 减少锁单数量
     * @param teamId 团购团队ID
     * @return 减少锁单数量
     */
    int updateSubtractionLockCount(String teamId);

    /**
     * 查询团购进度
     * @param teamId 团购团队ID
     * @return 团购进度
     */
    GroupBuyOrder queryGroupBuyProgress(String teamId);

    /**
     * 查询团购团队
     * @param teamId 团购团队ID
     * @return 团购团队
     */
    GroupBuyOrder queryGroupBuyTeamByTeamId(String teamId);

    /**
     * 订单完成
     * @param teamId 团购团队ID
     * @return 订单完成数量
     */
    int updateAddCompleteCount(String teamId);

    /**
     * 订单完成
     * @param teamId 团购团队ID
     * @return 订单完成数量
     */
    int updateOrderStatus2COMPLETE(String teamId);

    /**
     * 查询团购进度
     * @param teamIds 团购团队ID
     * @return 团购进度
     */
    List<GroupBuyOrder> queryGroupBuyProgressByTeamIds(@Param("teamIds") Set<String> teamIds);

    /**
     * 查询所有团购团队数量
     * @param teamIds 团购团队ID
     * @return 团购团队数量
     */
    Integer queryAllTeamCount(@Param("teamIds")Set<String> teamIds);

    /**
     * 订单退款
     * @param teamIds 团购团队ID
     * @return 退款订单数量
     */
    Integer queryAllTeamCompleteCount(@Param("teamIds")Set<String> teamIds);

    /**
     * 订单退款
     * @param teamIds 团购团队ID
     * @return 退款订单数量
     */
    Integer queryAllUserCount(@Param("teamIds")Set<String> teamIds);

    /**
     * 订单退款
     * @param groupBuyOrderReq 退款订单
     * @return 退款订单数量
     */
    int unpaid2Refund(GroupBuyOrder groupBuyOrderReq);

    /**
     * 取消团购
     * @param teamId 团购团队ID
     * @return 取消团购数量
     */
    int cancelTeam(String teamId);
}
