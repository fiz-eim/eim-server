package com.soflyit.common.mybatis.config;

import com.soflyit.common.mybatis.incrementer.SnowFlakeIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义Id生成器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-15 11:22
 */
@Configuration
@ConditionalOnProperty(name = "soflyit.mybatis-plus.custom-id-generator.enable", havingValue = "true")
public class SoflyMybatisIdConfig {

    @ConditionalOnProperty(name = "soflyit.mybatis-plus.custom-id-generator.generator-type", havingValue = "snowflake")
    @Bean
    public SnowFlakeIdGenerator snowFlakeIdGenerator() {
        return new SnowFlakeIdGenerator();
    }

}
