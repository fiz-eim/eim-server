package com.soflyit.common.id.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花id配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-09 17:44
 */
@Configuration
@ConfigurationProperties(prefix = "soflyit.snow-flake")
@Data
public class SoflySnowFlakeConfig {


    private Long epochDate = 1698768000000L;


    private int dataCenterId = 1;


    private Long workerIdCacheTime = 7200L;


    private Long maxWorkId = 32L;

}
