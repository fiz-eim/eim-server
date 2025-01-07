package com.soflyit.chattask.dx.common.base;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.base
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-14  12:55
 * @Description: 
 * @Version: 1.0
 */
@Data
public class RangeParam<T> {
    private T rangeStart;
    private T rangeEnd;
}
