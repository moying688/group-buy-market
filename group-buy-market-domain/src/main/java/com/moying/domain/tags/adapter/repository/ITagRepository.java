package com.moying.domain.tags.adapter.repository;

import com.moying.domain.tags.model.entity.CrowdTagsJobEntity;

/**
 * @Author: moying
 * @CreateTime: 2025-05-05
 * @Description: 标签仓储
 */

public interface ITagRepository {
    CrowdTagsJobEntity queryCrowdTagsJobEntity(String tagId, String batchId);

    void addCrowdTagsUserId(String tagId, String userId);

    void updateCrowdTagsStatistics(String tagId, int size);
}
