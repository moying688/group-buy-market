package com.moying.infrastructure.dcc;

import com.moying.types.annotations.DCCValue;
import com.moying.types.common.Constants;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-05-06
 * @Description:
 */


@Service
public class DCCService {


    @DCCValue("downgradeSwitch:0")
    private String downgradeSwitch;

    @DCCValue("cutRange:100")
    private  String cutRange;

    @DCCValue("scBlacklist:s02c02")
    private String scBlacklist;

    @DCCValue("cacheSwitch:0")
    private String cacheOpenSwitch;


    public boolean isDowngradeSwitch() {
        return "1".equals(downgradeSwitch);
    }
    public boolean isCutRange(String userId){
        // 计算用户id 哈希值
        int hash = Math.abs(userId.hashCode());
        // 计算哈希值与范围的模
        int lastTwoDigits = hash % 100;
        // 判断是否在切量范围内
        if (lastTwoDigits < Integer.parseInt(cutRange)) {
            return true;
        }
        return false;
    }

    public boolean isSCBlackIntercept(String source, String channel) {

        // todo controller 更新貌似有点问题 value为空时不会更新为空字符串
        List<String> list = Arrays.asList(scBlacklist.split(Constants.SPLIT));
        return list.contains(source + channel);
    }

    /**
     * 缓存开启开关，true为开启，1为关闭
     */
    public boolean isCacheOpenSwitch(){
        return "0".equals(cacheOpenSwitch);
    }
}
