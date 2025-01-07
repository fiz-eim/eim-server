package com.soflyit.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 头像服务配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-02 09:35
 */
@Configuration
@ConfigurationProperties(prefix = "soflyit.avatar")
@Data
public class AvatarConfig {


    private String storageBucket;


    private String accessUrl;


    private String avatarServer;

}
