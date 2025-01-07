package com.soflyit.common.security.config;

import com.soflyit.common.core.web.upload.UploadMultipartResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 文件上传进度配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-24 11:24
 */
public class FileUploadConfig {


    @ConditionalOnProperty(name = "soflyit.file.upload.progress", havingValue = "true")
    @Bean("multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        return new UploadMultipartResolver();
    }


}
