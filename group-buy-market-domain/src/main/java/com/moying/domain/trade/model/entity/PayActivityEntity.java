package com.moying.domain.trade.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: moying
 * @CreateTime: 2025-05-07
 * @Description:  拼团，支付活动实体对象
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayActivityEntity {


    /** 拼单组队ID */
    private String teamId;
    /** 活动ID */
    private Long activityId;
    /** 活动名称 */
    private String activityName;
    /** 拼团开始时间 */
    private Date startTime;
    /** 拼团结束时间 */
    private Date endTime;
    /** 目标数量 */
    private Integer targetCount;
}
