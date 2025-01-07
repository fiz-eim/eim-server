package com.soflyit.common.redis.configure;

import com.soflyit.common.redis.aspect.BaseModulesAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lichao
 * @Description
 * @create 2024-01-24 15:32
 */
@Configuration
@ConditionalOnProperty(prefix = "soflyit.baseModule.parse", name = "enable", havingValue = "true")
public class BaseModulesConfiguration {
    @Bean
    public BaseModulesAspect baseModulesAspect() {
        return new BaseModulesAspect();
    }
}
