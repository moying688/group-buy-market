package com.moying.domain.tags.service;

/**
 * @Author: moying
 * @CreateTime: 2025-05-05
 * @Description:人群标签服务接口
 */

public interface ITagService {

    /**
     * 执行人群标签批次任务
     *
     * @param tagId   人群ID
     * @param batchId 批次ID
     */
    void execTagBatchJob(String tagId, String batchId);
}
