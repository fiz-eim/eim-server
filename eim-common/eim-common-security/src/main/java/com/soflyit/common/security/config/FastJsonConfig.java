package com.soflyit.common.security.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.annotation.PostConstruct;

/**
 * fastjson 配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-23 14:25
 */
@ConditionalOnProperty(name = "soflyit.fastjson.long2String.enable", havingValue = "true")
public class FastJsonConfig {

    @PostConstruct
    private void initConfig() {
        SerializeConfig.getGlobalInstance().put(Long.class, new ToStringSerializer());
    }

}
