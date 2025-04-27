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


    List<GroupBuyDiscount> queryGroupBuyDiscountList();
}