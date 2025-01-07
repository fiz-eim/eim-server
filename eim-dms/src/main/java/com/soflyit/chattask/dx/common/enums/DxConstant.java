package com.soflyit.chattask.dx.common.enums;

import lombok.Data;

/**
 * Package: com.soflyit.chattask.dx.common.enums
 *
 * @Description:
 * @date: 2023/11/29 14:24
 * @author: dddgoal@163.com
 */
@Data
public class DxConstant {


    public static String RESOURCE_FOLDER_TYPE = "folder";


    public static String STATUS_IS_YES = "1";


    public static String STATUS_IS_NO = "2";


    public static Long PROJECT_FOLDER_ID = 7L;


    public static Long RECYCLE_FOLDER_ID = 5L;


    public static Long RECENT_FOLDER_ID = 1L;



    public static String NO_FOLDER_PERMISSION = "[]";



    public static final String[] FOLDER_ID_NAME = {"顶级","最近使用","我的文档","文件库","我的收藏","回收站","模板","项目","聊天"};



    public static String ALL_FOLDER_PERMISSION = "[1,2,3,4,5,6,7,8,9,10,11,12,13,14]";


    public static String DEFAULT_FOLDER_PERMISSION = "[1,2,3,4,5,6,7,8,9,10,11,12,13]";


    public static String PERMISSION_DELETE = "delete";


    public static Integer VIEW_SELF_PERMISSION = 1;


    public static Integer VIEW_ALL_PERMISSION = 2;



    public static Integer EDIT_SELF_PERMISSION = 5;


    public static Integer EDIT_ALL_PERMISSION = 6;


    public static Integer UPLOAD_FILE_PERMISSION = 11;


    public static Integer ADD_FOLDER_PERMISSION = 12;


    public static Integer DELETE_SELF_PERMISSION = 3;


    public static Integer DELETE_ALL_PERMISSION = 4;


    public static Integer DOWNLOAD_ALL_PERMISSION = 8;


    public static Integer DOWNLOAD_SELF_PERMISSION = 7;
}
