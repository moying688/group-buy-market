package com.moying.infrastructure.gateway;

import com.moying.types.enums.ResponseCode;
import com.moying.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description: 拼团回调服务
 */

@Service
@Slf4j
public class GroupBuyNotifyService {

    @Resource
    private OkHttpClient okHttpClient;


    public String groupBuyNotify(String apiUrl,String notifyRequestDTOJSON) throws Exception{
        try{
            // 1. 构建参数
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, notifyRequestDTOJSON);
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .build();

            // 2. 调用接口
            Response response = okHttpClient.newCall(request).execute();
            // 3. 返回结果
            return response.body().string();
        }catch  (Exception e) {
            // todo 考虑熔断，服务降级？
            log.error("拼团回调 HTTP 接口服务异常 {}", apiUrl, e);
            throw new AppException(ResponseCode.HTTP_EXCEPTION);
        }
    }
}
