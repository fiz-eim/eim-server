package com.soflyit.common.security.handler;

import org.apache.ibatis.reflection.MetaObject;

/**
 * Mybatisplus 自定义数据填充<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-23 10:17:48
 */
public interface CustomMetaObjectHandler {
    void insertFill(MetaObject metaObject);

    void updateFill(MetaObject metaObject);
}
