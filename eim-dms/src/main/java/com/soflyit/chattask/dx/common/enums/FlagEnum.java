package com.soflyit.chattask.dx.common.enums;

import lombok.Getter;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.enums
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-10  16:20
 * @Description: 基础标记定义
 * @Version: 1.0
 */
@Getter
public enum FlagEnum {
    DELETE(1, "DELETE", "删除标记"),
    NOT_DELETE(2, "DELETE", "删除标记"),
    ASC(1, "ORDER_By", "排序标记"),
    DESC(0, "ORDER_By", "排序标记");

    private final Integer code;
    private final String name;
    private final String desc;

    FlagEnum(int code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }
}
