package com.moying.trigger.http;

import com.moying.api.IDCCService;
import com.moying.api.response.Response;
import com.moying.types.enums.ResponseCode;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: moying
 * @CreateTime: 2025-05-06
 * @Description: 动态配置管理
 */


@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/dcc/")
public class DCCController implements IDCCService {


//    @Resource
//    private RTopic dccTopic;
//
//
//    /**
//     * 动态值变更
//     * <p>
//     * curl http://127.0.0.1:8091/api/v1/gbm/dcc/update_config?key=downgradeSwitch&value=1
//     * curl http://127.0.0.1:8091/api/v1/gbm/dcc/update_config?key=cutRange&value=0
//     */
//    @RequestMapping(value = "update_config", method = RequestMethod.GET)
//    @Override
//    public Response<Boolean> updateConfig(@RequestParam String key, @RequestParam String value) {
//        try {
//            log.info("DCC 动态配置值变更 key:{} value:{}", key, value);
//            dccTopic.publish(key + "," + value);
//            return Response.<Boolean>builder()
//                    .code(ResponseCode.SUCCESS.getCode())
//                    .info(ResponseCode.SUCCESS.getInfo())
//                    .build();
//        } catch (Exception e) {
//            log.error("DCC 动态配置值变更失败 key:{} value:{}", key, value, e);
//            return Response.<Boolean>builder()
//                    .code(ResponseCode.UN_ERROR.getCode())
//                    .info(ResponseCode.UN_ERROR.getInfo())
//                    .build();
//        }
//    }
private static final String BASE_PATH = "group_buy_market_dcc_";

    private final Client etcdClient;

    public DCCController() {
        this.etcdClient = Client.builder().endpoints("http://localhost:2379").build();
    }

    /**
     * 更新配置值（Etcd方式）
     * curl http://127.0.0.1:8091/api/v1/gbm/dcc/update_config?key=downgradeSwitch&value=1
     */
    @RequestMapping(value = "update_config", method = RequestMethod.GET)
    @Override
    public Response<Boolean> updateConfig(@RequestParam String key, @RequestParam String value) {
        try {
            String etcdKey = BASE_PATH + key;
            etcdClient.getKVClient()
                    .put(ByteSequence.from(etcdKey.getBytes()), ByteSequence.from(value.getBytes()))
                    .get();

            log.info("Etcd配置更新成功 key:{} value:{}", etcdKey, value);
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("Etcd配置更新失败 key:{} value:{}", key, value, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
