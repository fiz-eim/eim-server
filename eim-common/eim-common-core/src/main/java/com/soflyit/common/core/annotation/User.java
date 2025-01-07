package com.soflyit.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.soflyit.common.core.annotation.EntityName.USER_NAME_SUFFIX;
import static com.soflyit.common.core.annotation.EntityName.USER_REDIS_KEY;

/**
 * 部门注解
 * 在baseEntity的用户ID字段上使用 返回对应的人员昵称
 *
 * @Author: 李超
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityName(cacheKey = USER_REDIS_KEY, suffix = USER_NAME_SUFFIX)
public @interface User {
    String value() default "";
}
