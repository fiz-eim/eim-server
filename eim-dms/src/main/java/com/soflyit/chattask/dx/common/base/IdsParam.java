package com.soflyit.chattask.dx.common.base;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.base
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-13  09:06
 * @Description: 
 * @Version: 1.0
 */
@Data
public class IdsParam<T> {
    private List<T> ids;
}
