package com.moying.domain.tags.service;

import com.moying.domain.tags.adapter.repository.ITagRepository;
import com.moying.domain.tags.model.entity.CrowdTagsJobEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-05-05
 * @Description: 人群标签服务
 */


@Slf4j
@Service
public class TagService implements ITagService {


    @Resource
    private ITagRepository repository;

    public void execTagBatchJob(String tagId,String batchId){
        log.info("人群标签批次任务 tagId:{} batchId:{}", tagId, batchId);

        // 1. 查询批次任务
       CrowdTagsJobEntity crowdTagsJobEntity =  repository.queryCrowdTagsJobEntity(tagId,batchId);

       // 2. 采集数据

        //  3. 数据写入记录
        ArrayList<String> userIds = new ArrayList<String>(){{
            add("moying");
            add("my");
            add("test01");
            add("test02");
            add("test03");
        }};
        // 4. 批次处理
        for (String userId : userIds) {
            repository.addCrowdTagsUserId(tagId,userId);
        }

        // 5. 更新人群标签统计量
        repository.updateCrowdTagsStatistics(tagId,userIds.size());
    }
}
