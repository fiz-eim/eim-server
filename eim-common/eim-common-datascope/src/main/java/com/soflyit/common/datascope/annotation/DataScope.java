package com.soflyit.common.datascope.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 *
 * @author soflyit
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    String deptAlias() default "";


    String userAlias() default "";


    String deptFieldName() default "dept_id";


    String userFieldName() default "user_id";
}
