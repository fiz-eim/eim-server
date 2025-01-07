package com.soflyit.chattask.dx.common.enums;

import lombok.Getter;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.enums
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-14  19:31
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum AppLevelEnum {


    MY("home", "我的文档"),
    RECENT("recent", "最近使用"),
    GROUP("group", "团体应用"),
    FOLDER("folder", "普通应用");

    AppLevelEnum(String level, String desc) {
        this.level = level;
        this.desc = desc;
    }


    private final String level;
    private final String desc;


}
