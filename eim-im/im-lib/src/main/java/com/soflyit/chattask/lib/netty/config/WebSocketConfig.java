package com.soflyit.chattask.lib.netty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * websocket配置
 */
@Configuration
@ConfigurationProperties(prefix = "soflyit.im.websocket")
@Data
@Component
public class WebSocketConfig {


    private String ip;


    private int port = 9501;


    private int maxFrameSize = 90000;


    private String path = "/chat";


    private int maxClientCount = 20000;


    private String origin = "*";


}
