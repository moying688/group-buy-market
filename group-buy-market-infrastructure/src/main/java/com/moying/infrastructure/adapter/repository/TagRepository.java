package com.moying.infrastructure.adapter.repository;


import com.moying.domain.tags.adapter.repository.ITagRepository;
import com.moying.domain.tags.model.entity.CrowdTagsJobEntity;
import com.moying.infrastructure.dao.ICrowdTagsDao;
import com.moying.infrastructure.dao.ICrowdTagsDetailDao;
import com.moying.infrastructure.dao.ICrowdTagsJobDao;
import com.moying.infrastructure.dao.po.CrowdTags;
import com.moying.infrastructure.dao.po.CrowdTagsDetail;
import com.moying.infrastructure.dao.po.CrowdTagsJob;
import com.moying.infrastructure.redis.IRedisService;
import lombok.Builder;
import org.redisson.api.RBitSet;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description 人群标签仓储
 */

@Repository
public class TagRepository implements ITagRepository {

    @Resource
    private ICrowdTagsDao crowdTagsDao;
    @Resource
    private ICrowdTagsDetailDao crowdTagsDetailDao;
    @Resource
    private ICrowdTagsJobDao crowdTagsJobDao;

    @Resource
    private IRedisService redisService;


    @Override
    public CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId) {
        CrowdTagsJob crowdTagsJob = new CrowdTagsJob();
        crowdTagsJob.setTagId(tagId);
        crowdTagsJob.setBatchId(batchId);
        CrowdTagsJob dbBean = crowdTagsJobDao.queryCrowdTagsJob(crowdTagsJob);
        if (null == dbBean) {
            return null;
        }

        return CrowdTagsJobEntity.builder()
                .tagType(dbBean.getTagType())
                .tagRule(dbBean.getTagRule())
                .statStartTime(dbBean.getStatStartTime())
                .statEndTime(dbBean.getStatEndTime())
                .build();
    }

    @Override
    public void addCrowdTagsUserId(String tagId, String userId) {
        CrowdTagsDetail crowdTagsDetail = new CrowdTagsDetail();
        crowdTagsDetail.setTagId(tagId);
        crowdTagsDetail.setUserId(userId);
        try {
            crowdTagsDetailDao.addCrowdTagsUserId(crowdTagsDetail);
            // 写入缓存
            RBitSet bitSet = redisService.getBitSet(tagId);
            bitSet.set(redisService.getIndexFromUserId(userId));
        } catch (DuplicateKeyException e) {
            // 处理重复key异常
            // 忽略唯一索引冲突
        }
    }

    @Override
    public void updateCrowdTagsStatistics(String tagId, int count) {
        CrowdTags crowdTags = new CrowdTags();
        crowdTags.setTagId(tagId);
        crowdTags.setStatistics(count);
        crowdTagsDao.updateCrowdTagsStatistics(crowdTags);
    }
}
