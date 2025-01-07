package com.soflyit.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.soflyit.common.core.annotation.EntityName.DEPART_NAME_SUFFIX;
import static com.soflyit.common.core.annotation.EntityName.DEPART_REDIS_KEY;

/**
 * 部门注解
 * 在baseEntity的部门字段上使用 返回对应的部门名称
 *
 * @Author: lichao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@EntityName(suffix = DEPART_NAME_SUFFIX, cacheKey = DEPART_REDIS_KEY)
public @interface Depart {
    String value() default "";
}
