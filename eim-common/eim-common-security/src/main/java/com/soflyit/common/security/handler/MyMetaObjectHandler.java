package com.soflyit.common.security.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author yangd
 * @version 1.0
 * @date 2022/4/7 15:53
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private CustomMetaObjectHandler customMetaObjectHandler;

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.getValue("createTime") == null) {
            this.setFieldValByName("createTime", Calendar.getInstance().getTime(), metaObject);
        }

        if (metaObject.getValue("updateTime") == null) {
            this.setFieldValByName("updateTime", Calendar.getInstance().getTime(), metaObject);
        }
        if (customMetaObjectHandler != null) {
            customMetaObjectHandler.insertFill(metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", Calendar.getInstance().getTime(), metaObject);
        if (customMetaObjectHandler != null) {
            customMetaObjectHandler.updateFill(metaObject);
        }
    }

    @Autowired(required = false)
    public void setCustomMetaObjectHandler(CustomMetaObjectHandler customMetaObjectHandler) {
        this.customMetaObjectHandler = customMetaObjectHandler;
    }
}
