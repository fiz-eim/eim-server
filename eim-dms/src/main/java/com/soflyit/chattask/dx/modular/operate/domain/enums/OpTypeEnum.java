package com.soflyit.chattask.dx.modular.operate.domain.enums;

import lombok.Getter;

@Getter
public enum OpTypeEnum {

    FOLDER_ADD("FOLDER_ADD", "创建了文件夹"),
    FOLDER_RENAME("FOLDER_RENAME", "重命名文件夹"),
    FOLDER_REMOVE("FOLDER_REMOVE", "删除了文件夹"),
    FOLDER_RECYCLE("FOLDER_RECYCLE", "还原了文件夹"),
    FOLDER_UPLOAD("FOLDER_UPLOAD", "上传文件夹"),


    FILE_ADD("FILE_ADD", "创建了文件"),
    FILE_REMOVE("FILE_REMOVE", "删除了文件"),
    FILE_RECYCLE("FILE_RECYCLE", "还原了文件"),
    FILE_UPLOAD("FILE_UPLOAD", "上传了文件"),
    FILE_RENAME("FILE_RENAME", "重命名文件"),


    FOLDER_PERMISSION_ADD("FOLDER_PERMISSION_ADD", "新增了文件夹权限"),
    FOLDER_PERMISSION_DEL("FOLDER_PERMISSION_DEL", "删除了文件夹权限"),
    FOLDER_PERMISSION_UPDATE("FOLDER_PERMISSION_UPDATE", "修改了文件夹权限"),


    FILE_RESTORE("FILE_RESTORE", "文件版本回滚"),
    FILE_VERSION_REMARK("FILE_VERSION_REMARK", "编辑了文件版本描述"),
    FILE_VERSION_UPDATE("FILE_VERSION_UPDATE", "更新了文件版本"),



    USER_ADD("USER_ADD", "添加了用户"),
    USER_REMOVE("USER_REMOVE", "移除了用户");


    /*   FOLDER_RECYCLE(),
       FOLDER_RECYCLE(),*/


    private final String code;
    private final String name;


    OpTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


}
