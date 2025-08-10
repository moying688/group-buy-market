package com.moying.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: moying
 * @CreateTime: 2025-05-11
 * @Description: 团购订单状态枚举
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum GroupBuyOrderEnumVO {

    /**
     * 拼单中
     */
    PROGRESS(0, "拼单中"),
    /**
     * 完成
     */
    COMPLETE(1, "完成"),
    /**
     * 失败
     */
    FAIL(2, "失败"),
    /**
     * 完成-含退单
     */
    COMPLETE_FAIL(3, "完成-含退单"),
    ;

    /**
     * 团购订单状态枚举
     */
    private Integer code;
    /**
     * 团购订单状态枚举描述
     */
    private String info;

    /**
     * 团购订单状态枚举值of
     * @param code 团购订单状态枚举值
     * @return 团购订单状态枚举
     */
    public static GroupBuyOrderEnumVO valueOf(Integer code) {
        switch (code) {
            case 0:
                return PROGRESS;
            case 1:
                return COMPLETE;
            case 2:
                return FAIL;
            case 3:
                return COMPLETE_FAIL;
            default:
                throw new RuntimeException("err code not exist!");
        }
    }

}