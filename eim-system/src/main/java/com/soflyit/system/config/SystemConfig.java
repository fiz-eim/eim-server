package com.soflyit.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system")
@Data
public class SystemConfig {

    private String clientId;

    private Long appId;


    private String defaultPassword = "fizEim@123^";

}
