package com.moying.infrastructure.dao;


import com.moying.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 拼团折扣表数据访问对象接口
 * @author moying
 */
@Mapper
public interface IGroupBuyDiscountDao {


    /**
     * 查询拼团折扣表列表
     * @return 拼团折扣表列表
     */
    List<GroupBuyDiscount> queryGroupBuyDiscountList();

    /**
     * 根据折扣ID查询拼团折扣表
     * @param discountId 折扣ID
     * @return 拼团折扣表
     */
    GroupBuyDiscount queryGroupBuyDiscountByDiscountId(String discountId);
}