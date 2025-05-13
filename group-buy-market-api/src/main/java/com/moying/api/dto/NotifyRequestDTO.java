package com.moying.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: moying
 * @CreateTime: 2025-05-13
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyRequestDTO {
    /** 组队ID */
    private String teamId;
    /** 外部单号 */
    private List<String> outTradeNoList;
}
