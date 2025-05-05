package com.moying.infrastructure.adapter.repository;

import com.moying.domain.activity.adapter.repository.IActivityRepository;
import com.moying.domain.activity.model.valobj.DiscountTypeEnum;
import com.moying.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import com.moying.domain.activity.model.valobj.SCSkuActivityVO;
import com.moying.domain.activity.model.valobj.SkuVO;
import com.moying.infrastructure.dao.IGroupBuyActivityDao;
import com.moying.infrastructure.dao.IGroupBuyDiscountDao;
import com.moying.infrastructure.dao.ISCSkuActivityDao;
import com.moying.infrastructure.dao.ISkuDao;
import com.moying.infrastructure.dao.po.GroupBuyActivity;
import com.moying.infrastructure.dao.po.GroupBuyDiscount;
import com.moying.infrastructure.dao.po.SCSkuActivity;
import com.moying.infrastructure.dao.po.Sku;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-04-27
 * @Description:
 */


@Repository
public class ActivityRepository implements IActivityRepository {



    @Resource
    private IGroupBuyActivityDao groupBuyActivityDao;
    @Resource
    private IGroupBuyDiscountDao groupBuyDiscountDao;
    @Resource
    private ISkuDao skuDao;
    @Resource
    private ISCSkuActivityDao skuActivityDao;

    @Override
    public GroupBuyActivityDiscountVO queryGroupBuyActivityDiscountVO(Long activityId) {
        // 构建查询条件  根据SC渠道值查询配置中最新的1个有效的活动

        GroupBuyActivity groupBuyActivityRes = groupBuyActivityDao.queryByActivityId(activityId);
        if (null == groupBuyActivityRes) return null;

        // 根据活动ID查询活动折扣信息
        String discountId = groupBuyActivityRes.getDiscountId();
        GroupBuyDiscount groupBuyDiscountRes = groupBuyDiscountDao.queryGroupBuyDiscountByDiscountId(discountId);


        GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount = GroupBuyActivityDiscountVO.GroupBuyDiscount.builder()
                .discountName(groupBuyDiscountRes.getDiscountName())
                .discountDesc(groupBuyDiscountRes.getDiscountDesc())
                .discountType(DiscountTypeEnum.get(groupBuyDiscountRes.getDiscountType()))
                .marketPlan(groupBuyDiscountRes.getMarketPlan())
                .marketExpr(groupBuyDiscountRes.getMarketExpr())
                .tagId(groupBuyDiscountRes.getTagId())
                .build();

        return GroupBuyActivityDiscountVO.builder()
                .activityId(groupBuyActivityRes.getActivityId())
                .activityName(groupBuyActivityRes.getActivityName())
                .groupBuyDiscount(groupBuyDiscount)
                .groupType(groupBuyActivityRes.getGroupType())
                .takeLimitCount(groupBuyActivityRes.getTakeLimitCount())
                .target(groupBuyActivityRes.getTarget())
                .validTime(groupBuyActivityRes.getValidTime())
                .status(groupBuyActivityRes.getStatus())
                .startTime(groupBuyActivityRes.getStartTime())
                .endTime(groupBuyActivityRes.getEndTime())
                .tagId(groupBuyActivityRes.getTagId())
                .tagScope(groupBuyActivityRes.getTagScope())
                .build();
    }

    @Override
    public SkuVO querySkuByGoodsId(String goodsId) {
        Sku sku = skuDao.querySkuByGoodsId(goodsId);
        if (null == sku) return null;
        return SkuVO.builder()
                .goodsId(sku.getGoodsId())
                .goodsName(sku.getGoodsName())
                .originalPrice(sku.getOriginalPrice())
                .build();
    }

    @Override
    public SCSkuActivityVO querySCSkuActivityBySCGoodsId(String source, String channel, String goodsId) {
        SCSkuActivity scSkuActivityReq = new SCSkuActivity();
        scSkuActivityReq.setSource(source);
        scSkuActivityReq.setChannel(channel);
        scSkuActivityReq.setGoodsId(goodsId);

        SCSkuActivity scSkuActivity = skuActivityDao.querySCSkuActivityBySCGoodsId(scSkuActivityReq);
        if (null == scSkuActivity) return null;
        return SCSkuActivityVO.builder()
                .source(scSkuActivity.getSource())
                .chanel(scSkuActivity.getChannel())
                .activityId(scSkuActivity.getActivityId())
                .goodsId(scSkuActivity.getGoodsId())
                .build();
    }
}
