
package com.soflyit.chattask.dx.common.global;

import cn.hutool.core.util.ObjectUtil;
import com.soflyit.chattask.dx.common.enums.FlagEnum;
import com.soflyit.common.security.handler.MyMetaObjectHandler;
import com.soflyit.common.security.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

/**
 * 配置
 *
 * @author JiangNing.G
 * @date 2023/11/9 14:24
 **/
@Configuration
public class GlobalConfigure implements WebMvcConfigurer {


    @Primary
    @Component
    public static class CustomMetaObjectHandler extends MyMetaObjectHandler {


        private static final String DELETE_FLAG = "deleteFlag";


        private static final String CREATE_USER = "createBy";


        private static final String CREATE_TIME = "createTime";


        private static final String UPDATE_USER = "updateBy";


        private static final String UPDATE_TIME = "updateTime";

        @Override
        public void insertFill(MetaObject metaObject) {

            try {

                Object deleteFlag = metaObject.getValue(DELETE_FLAG);
                if (ObjectUtil.isNull(deleteFlag)) {
                    setFieldValByName(DELETE_FLAG, FlagEnum.NOT_DELETE.getCode(), metaObject);
                }
            } catch (ReflectionException ignored) {
                System.out.println();
            }
            try {

                Object createUser = metaObject.getValue(CREATE_USER);
                if (ObjectUtil.isNull(createUser)) {
                    setFieldValByName(CREATE_USER, SecurityUtils.getUserId(), metaObject);
                }
            } catch (ReflectionException ignored) {
                System.out.println();

            }
            try {

                Object createTime = metaObject.getValue(CREATE_TIME);
                if (ObjectUtil.isNull(createTime)) {
                    setFieldValByName(CREATE_TIME, new Date(), metaObject);
                }
            } catch (ReflectionException ignored) {
                System.out.println();

            }
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            try {

                setFieldValByName(UPDATE_USER, SecurityUtils.getUserId(), metaObject);
            } catch (ReflectionException ignored) {
            }
            try {

                setFieldValByName(UPDATE_TIME, new Date(), metaObject);
            } catch (ReflectionException ignored) {
            }
        }
    }

}
