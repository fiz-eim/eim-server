package com.soflyit.common.security.config;

import com.soflyit.common.security.interceptor.HeaderInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author soflyit
 */
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String[] excludeUrls = {"/login", "/logout", "/refresh"};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getHeaderInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludeUrls)
                .order(-10);
    }


    public HeaderInterceptor getHeaderInterceptor() {
        return new HeaderInterceptor();
    }
}
