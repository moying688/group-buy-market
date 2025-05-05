package com.moying.infrastructure.dao;

import com.moying.infrastructure.dao.po.CrowdTagsJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author moying
 * @description 人群标签任务
 */
@Mapper
public interface ICrowdTagsJobDao {


    CrowdTagsJob queryCrowdTagsJob(CrowdTagsJob crowdTagsJobReq);

}
