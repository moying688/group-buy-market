package com.moying.infrastructure.dao;


import com.moying.infrastructure.dao.po.GroupBuyActivity;
import com.moying.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 拼团活动数据访问对象接口
 * @author moying
 *
 */
@Mapper
public interface IGroupBuyActivityDao {

    /**
     * 查询拼团活动表列表
     * @return 拼团活动表列表
     */
    List<GroupBuyActivity> queryGroupBuyActivityList();



    GroupBuyActivity queryByActivityId(Long activityId);
}