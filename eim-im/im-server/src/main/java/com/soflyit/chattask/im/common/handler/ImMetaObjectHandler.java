package com.soflyit.chattask.im.common.handler;

import com.soflyit.common.security.handler.CustomMetaObjectHandler;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充数据服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-23 10:27
 */
@Component
@Slf4j
public class ImMetaObjectHandler implements CustomMetaObjectHandler {

    private static final String FIELD_CREATE_BY_NAME = "createBy";
    private static final String FIELD_UPDATE_BY_NAME = "updateBy";
    private static final String FIELD_UPDATE_TIME = "updateTime";
    private static final String FIELD_CREATE_TIME = "createTime";


    @Override
    public void insertFill(MetaObject metaObject) {
        LoginUser user = SecurityUtils.getLoginUser();
        Date now = new Date();
        log.debug("mybatis plus auto fill - insert ");
        if (user != null) {
            if (metaObject.getValue(FIELD_CREATE_BY_NAME) == null) {
                metaObject.setValue(FIELD_CREATE_BY_NAME, user.getUserid());
            }
            if (metaObject.getValue(FIELD_UPDATE_BY_NAME) == null) {
                metaObject.setValue(FIELD_UPDATE_BY_NAME, user.getUserid());
            }
        } else {
            log.warn("insert fill failed. user is null");
        }

        if (metaObject.getValue(FIELD_UPDATE_TIME) == null) {
            metaObject.setValue(FIELD_UPDATE_TIME, now);
        }
        if (metaObject.getValue(FIELD_CREATE_TIME) == null) {
            metaObject.setValue(FIELD_CREATE_TIME, now);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LoginUser user = SecurityUtils.getLoginUser();
        if (user != null) {
            metaObject.setValue(FIELD_UPDATE_BY_NAME, user.getUserid());
        } else {
            log.warn("update fill failed. user is null");
        }

        Date now = new Date();
        if (metaObject.getValue(FIELD_CREATE_TIME) == null) {
            metaObject.setValue(FIELD_CREATE_TIME, now);
        }
    }
}
