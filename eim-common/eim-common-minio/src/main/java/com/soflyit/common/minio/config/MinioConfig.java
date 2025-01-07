package com.soflyit.common.minio.config;

import com.soflyit.common.minio.service.MinioService;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 配置信息
 *
 * @author soflyit
 */
@Configuration
@ConfigurationProperties(prefix = "soflyit.minio")
@ConditionalOnProperty(name = "soflyit.minio.enable", havingValue = "true")
@Data
public class MinioConfig {

    private String url;


    private String accUrl;


    private String accessKey;


    private String secretKey;


    private String bucketName;


    private String prefix = "minio";


    private Boolean progress = Boolean.FALSE;


    private int updateIntervalMillis = 100;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
    }

    @Bean
    public MinioService minioService() {
        return new MinioService();
    }
}
