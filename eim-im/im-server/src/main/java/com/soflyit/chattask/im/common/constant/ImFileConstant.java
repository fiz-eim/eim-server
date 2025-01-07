package com.soflyit.chattask.im.common.constant;

/**
 * 文件常量<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-11 16:45
 */
public interface ImFileConstant {


    Short FILE_STORAGE_TYPE_LOCAL = 1;

    Short FILE_STORAGE_TYPE_SOFLY_DX = 2;


    Short FILE_STORAGE_TYPE_MINIO = 3;

    String PREVIEW_SUFFIX = "_preview";

    int MAX_PREVIEW_WIDTH = 1920;
    int MAX_PREVIEW_HEIGHT = 1920;

    String THUMBNAIL_SUFFIX = "_thumbnail";
    int MAX_THUMBNAIL_WIDTH = 120;
    int MAX_THUMBNAIL_HEIGHT = 120;


    Short PREVIEW_FLAG_TRUE = 1;
    Short PREVIEW_FLAG_FALSE = 2;


    Short FILE_TYPE_ORIGINAL = 1;
    Short FILE_TYPE_PREVIEW = 2;
    Short FILE_TYPE_THUMBNAIL = 4;


    Short FAILED_FILE_STATUS_WAIT = 1;
    Short FAILED_FILE_STATUS_RETRYING = 2;
    Short FAILED_FILE_STATUS_FAILED = 3;


}
