package com.moying.infrastructure.dcc;

import com.moying.types.annotations.DCCValue;
import org.springframework.stereotype.Service;

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
}
