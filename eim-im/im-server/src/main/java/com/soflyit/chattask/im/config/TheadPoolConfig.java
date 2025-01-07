package com.soflyit.chattask.im.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TheadPoolConfig {

    @Autowired
    private SoflyThreadConfig soflyThreadConfig;

    @Bean("SoflyExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(soflyThreadConfig.getCorePoolSize());
        executor.setMaxPoolSize(soflyThreadConfig.getMaxPoolSize());
        executor.setQueueCapacity(soflyThreadConfig.getQueueCapacity());
        executor.setKeepAliveSeconds(soflyThreadConfig.getKeepAliveSeconds());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("sofly-executor-");
        return executor;
    }


}
