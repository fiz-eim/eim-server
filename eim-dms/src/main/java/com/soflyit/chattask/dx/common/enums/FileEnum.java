package com.soflyit.chattask.dx.common.enums;

import lombok.Getter;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.enums
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-13  18:14
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum FileEnum {

    IMAGE("image", new String[]{"bmp","tif","pcx","tga","exif","fpx","svg","psd","cdr","pcd","dxf",
            "ufo","eps","ai","raw","WMF","webp","tiff", "gif", "png", "jpeg", "jpg"}, "图片"),
    DOCUMENT("document", new String[]{"pdf","xls","xlsx", "doc", "docx", "ini", "jpeg", "jpg"}, "文档");

    FileEnum(String name, String[] types, String desc) {
        this.name = name;
        this.types = types;
        this.desc = desc;
    }


    private final String name;
    private final String[] types;
    private final String desc;
}
