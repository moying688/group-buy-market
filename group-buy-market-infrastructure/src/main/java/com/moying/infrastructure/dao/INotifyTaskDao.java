package com.moying.infrastructure.dao;

import com.moying.infrastructure.dao.po.NotifyTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description:
 */

@Mapper
public interface INotifyTaskDao {

    void insert(NotifyTask notifyTask);
}
