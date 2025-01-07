package com.soflyit.common.security.annotation;

import java.lang.annotation.*;

/**
 * 内部认证注解
 *
 * @author soflyit
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth {

    boolean isUser() default false;
}
