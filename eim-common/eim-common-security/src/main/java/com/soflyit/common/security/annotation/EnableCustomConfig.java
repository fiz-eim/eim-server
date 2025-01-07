package com.soflyit.common.security.annotation;

import com.soflyit.common.security.config.ApplicationConfig;
import com.soflyit.common.security.feign.FeignAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@EnableAspectJAutoProxy(exposeProxy = true)

@MapperScan("com.soflyit.**.mapper")

@EnableAsync

@Import({ApplicationConfig.class, FeignAutoConfiguration.class})
public @interface EnableCustomConfig {

}
