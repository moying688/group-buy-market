package com.moying.infrastructure.dao;

import com.moying.infrastructure.dao.po.GroupBuyOrderList;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description:
 */


@Mapper
public interface IGroupBuyOrderListDao {

    void insert(GroupBuyOrderList groupBuyOrderList);

    GroupBuyOrderList queryGroupBuyOrderRecordByOutTradeNo(GroupBuyOrderList groupBuyOrderList);

    Integer queryOrderCountByActivityId(GroupBuyOrderList groupBuyOrderList);
}
