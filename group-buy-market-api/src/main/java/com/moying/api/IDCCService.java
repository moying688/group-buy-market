package com.moying.api;

import com.moying.api.response.Response;

/**
 * @Author: moying
 * @CreateTime: 2025-05-06
 * @Description: 动态配置中心
 */

public interface IDCCService {

    Response<Boolean> updateConfig(String key, String value);

}
