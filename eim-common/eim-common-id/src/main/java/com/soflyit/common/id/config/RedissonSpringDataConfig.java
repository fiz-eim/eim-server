package com.soflyit.common.id.config;

import com.soflyit.common.id.config.redisson.FastJsonRedissonCodec;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * redisson配置<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-09 17:05
 */
@Configuration
public class RedissonSpringDataConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(RedisProperties redisProperties) {
        Config config = new Config();
        config.setLockWatchdogTimeout(30000);
        config.setCodec(new FastJsonRedissonCodec());
        String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(address);
        singleServerConfig.setPassword(redisProperties.getPassword());
        return Redisson.create(config);
    }

}
