package com.soflyit.chattask.dx.common;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-14  19:50
 * @Description: 
 * @Version: 1.0
 */
@Data
public class CheckVO {
    private boolean success;
    private String message;

    private CheckVO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static CheckVO success(String message) {
        return new CheckVO(true, message);
    }

    public static CheckVO error(String message) {
        return new CheckVO(false, message);
    }
}
