package com.moying.infrastructure.dao;


import com.moying.infrastructure.dao.po.GroupBuyDiscount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IGroupBuyDiscountDao {


    List<GroupBuyDiscount> queryGroupBuyDiscountList();
}