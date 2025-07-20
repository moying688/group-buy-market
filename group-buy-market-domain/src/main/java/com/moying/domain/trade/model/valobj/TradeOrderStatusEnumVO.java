package com.moying.domain.trade.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description: 交易订单状态枚举
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TradeOrderStatusEnumVO {


    /**
     * 创建
     */
    CREATE(0, "初始创建"),
    /**
     * 完成
     */
    COMPLETE(1, "消费完成"),
    /**
     * 关闭
     */
    CLOSE(2, "用户退单"),
    ;

    private Integer code;
    private String info;

    public static TradeOrderStatusEnumVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return CREATE;
            case 1:
                return COMPLETE;
            case 2:
                return CLOSE;
        }
        return CREATE;
    }

}
