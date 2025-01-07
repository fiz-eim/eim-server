package com.soflyit.common.core.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 根据id自动添加名称<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-26 13:54:34
 */
@Inherited
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityName {

    String DEPART_REDIS_KEY = "system:depart:name";

    String USER_REDIS_KEY = "system:user:nickName";

    String USER_NAME_SUFFIX = "__nickName";

    String DEPART_NAME_SUFFIX = "__departName";



    @AliasFor("fileName")
    String value() default "";


    @AliasFor("value")
    String fileName() default "";


    String suffix() default "";


    String cacheKey();

}
