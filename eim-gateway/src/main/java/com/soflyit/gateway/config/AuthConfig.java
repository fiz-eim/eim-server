package com.soflyit.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 类说明
 *
 * @author Toney
 * @date 2023-06-26
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private String encodePwdRule = "none";
}
