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


    void insert(GroupBuyOrder groupBuyOrder);

    int updateAddLockCount(String teamId);


    int updateSubtractionLockCount(String teamId);

    GroupBuyOrder queryGroupBuyProgress(String teamId);

    GroupBuyOrder queryGroupBuyTeamByTeamId(String teamId);

    int updateAddCompleteCount(String teamId);

    int updateOrderStatus2COMPLETE(String teamId);

    List<GroupBuyOrder> queryGroupBuyProgressByTeamIds(@Param("teamIds") Set<String> teamIds);

    Integer queryAllTeamCount(@Param("teamIds")Set<String> teamIds);

    Integer queryAllTeamCompleteCount(@Param("teamIds")Set<String> teamIds);

    Integer queryAllUserCount(@Param("teamIds")Set<String> teamIds);
}
