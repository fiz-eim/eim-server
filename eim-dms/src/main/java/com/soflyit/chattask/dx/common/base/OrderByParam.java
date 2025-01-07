package com.soflyit.chattask.dx.common.base;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.base
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-09  20:22
 * @Description: 字段排序结构体
 * @Version: 1.0
 */
@Data
public class OrderByParam<T> {
    private T fieldValue;
    private Integer orderType;
}
