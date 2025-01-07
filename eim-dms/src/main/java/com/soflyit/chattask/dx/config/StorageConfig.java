package com.soflyit.chattask.dx.config;

import com.soflyit.chattask.dx.modular.storage.impl.LocalStorageService;
import com.soflyit.chattask.dx.modular.storage.impl.MinIoStorageService;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-22 15:19
 */
@Configuration
@ConfigurationProperties("soflyit.dx.storage")
@Data
public class StorageConfig {


    private String type = "local";


    private String minioBucket = "chat-dx";


    @Bean
    @ConditionalOnProperty(name = "soflyit.dx.storage.type", havingValue = "local", matchIfMissing = true)
    public LocalStorageService localStorageService() {
        return new LocalStorageService();
    }

    @Bean
    @ConditionalOnProperty(name = "soflyit.dx.storage.type", havingValue = "minio")
    public MinIoStorageService minIoStorageService() {
        return new MinIoStorageService();
    }


}
