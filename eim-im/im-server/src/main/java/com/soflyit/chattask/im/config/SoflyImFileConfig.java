package com.soflyit.chattask.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 聊天文件配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-11 11:41
 */
@Configuration
@ConfigurationProperties(prefix = "soflyit.im.file")
@Data
@Component
public class SoflyImFileConfig {


    private Short storageType = 1;


    private String localPath = "storage/im/";


    private Long dxImFoldId = 10L;


    private String minioBucket = "chat-im";


    private String msgFileTmpDir = "msg-file-tmp";



    private Boolean enableClearUnMatchTempFile = Boolean.FALSE;


}
