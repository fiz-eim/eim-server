package com.soflyit.chattask.dx.common.enums;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作类型
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.common.enums
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-13  18:08
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum OperationType {


    UPLOAD("UPLOAD", "文件上传"),
    DOWNLOAD("DOWNLOAD", "文件下载"),
    NEW("NEW", "新建文件"),
    RENAME("RENAME", "文件重命名"),
    EDIT("EDIT", "文件编辑"),
    QUERY("QUERY", "文件查阅"),
    REMOVE("REMOVE", "文件查阅");
    private final String code;
    private final String desc;

    OperationType(String code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    @Getter
    public enum BodyType {

        FILE_UPLOAD("FILE_UPLOAD", "文件上传", UPLOAD),

        FILE_DOWNLOAD("FILE_DOWNLOAD", "文件下载", DOWNLOAD),

        FILE_NEW("FILE_NEW", "新建文件", NEW),

        FILE_RENAME("FILE_RENAME", "文件重命名", RENAME),

        FILE_EDIT("FILE_EDIT", "文件编辑", EDIT),

        FILE_QUERY("FILE_QUERY", "文件查阅", QUERY),

        FILE_REMOVE("FILE_REMOVE", "文件删除", REMOVE),

        FOLDER_UPLOAD("FILE_UPLOAD", "文件上传", UPLOAD),

        FOLDER_DOWNLOAD("FILE_DOWNLOAD", "文件下载", DOWNLOAD),

        FOLDER_NEW("FILE_NEW", "新建文件", NEW),

        FOLDER_RENAME("FILE_RENAME", "文件重命名", RENAME),

        FOLDER_EDIT("FILE_EDIT", "文件编辑", EDIT),

        FOLDER_QUERY("FILE_QUERY", "文件查阅", QUERY),

        FOLDER_REMOVE("FILE_REMOVE", "文件删除", REMOVE);

        private final OperationType type;
        private final String code;
        private final String desc;

        BodyType(String code, String desc, OperationType type) {
            this.type = type;
            this.desc = desc;
            this.code = code;
        }


        public Map<String, String> descInfo() {
            Map<String, String> desc = new HashMap<>();
            desc.put("code", this.getCode());
            desc.put("desc", this.getDesc());
            return desc;
        }
    }


}
