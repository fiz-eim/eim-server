package com.soflyit.chattask.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Im系统配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-24 14:49
 */
@Configuration
@ConfigurationProperties(prefix = "soflyit.im.message")
@Data
@Component
public class SoflyImConfig {


    private Boolean echoPush = Boolean.FALSE;


    private List<String> echoEventList = new ArrayList<>();


    private String avatarServer;

    public String getAvatarServer() {
        return avatarServer;
    }

    public void setAvatarServer(String avatarServer) {
        this.avatarServer = avatarServer;
    }
}
