package com.soflyit.common.id.util;

import cn.hutool.core.lang.Snowflake;
import com.soflyit.common.id.config.SoflySnowFlakeConfig;
import com.soflyit.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ID生成器 <br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-11-16 17:42
 */
@Component
@Slf4j
public class SnowflakeIdUtil {

    private static Snowflake snowflake;

    private SoflySnowFlakeConfig soflySnowFlakeConfig;

    private RedissonClient redissonClient;

    private RedisService redisService;

    private static final String SNOW_FLAKE_WORKER_ID_PREFIX = "snow-flake:";

    private long workerId = 0L;

    private String lockKey = null;

    private Boolean shutdownFlag = Boolean.FALSE;


    public static synchronized Long nextId() {
        return snowflake.nextId();
    }

    @PostConstruct
    private void init() {
        this.lockKey = SNOW_FLAKE_WORKER_ID_PREFIX + this.soflySnowFlakeConfig.getDataCenterId();
        initSnowFlake();
        startRenewTask();
    }

    private void startRenewTask() {

        Runnable task = new WorkerIdRenewTask(this);
        new Thread(task, "snowflake-workerId-renew-task").start();
    }

    private void initSnowFlake() {
        long datacenterId = soflySnowFlakeConfig.getDataCenterId();
        String lockKey = SNOW_FLAKE_WORKER_ID_PREFIX + datacenterId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.tryLock(10, TimeUnit.SECONDS);
            this.workerId = getWorkerId();
            snowflake = new Snowflake(new Date(soflySnowFlakeConfig.getEpochDate()), workerId, datacenterId, Boolean.FALSE);
            log.debug("snowFlake 初始化成功，datacenter:{},workerId:{}", datacenterId, workerId);
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new RuntimeException(e);
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    private void renewWorkId() {
        String workerIdKey = lockKey + ":" + workerId;
        if (redisService.hasKey(workerIdKey)) {
            redisService.expire(workerIdKey, soflySnowFlakeConfig.getWorkerIdCacheTime(), TimeUnit.SECONDS);
        } else {

            this.workerId = 0L;
            this.initSnowFlake();
        }
    }

    private long getWorkerId() {
        while (workerId <= soflySnowFlakeConfig.getMaxWorkId()) {
            String workerIdKey = lockKey + ":" + workerId;
            if (redisService.hasKey(workerIdKey)) {
                workerId++;
            } else {
                redisService.setCacheObject(workerIdKey, Boolean.TRUE, soflySnowFlakeConfig.getWorkerIdCacheTime(), TimeUnit.SECONDS);
                return workerId;
            }
        }
        return -1;
    }

    @PreDestroy
    private void shutdown() {
        this.shutdownFlag = Boolean.FALSE;
        this.releaseWorkerId();
    }

    private void releaseWorkerId() {
        String workerIdKey = lockKey + ":" + workerId;
        redisService.expire(workerIdKey, 0L);
    }

    @Autowired
    public void setSoflySnowFlakeConfig(SoflySnowFlakeConfig soflySnowFlakeConfig) {
        this.soflySnowFlakeConfig = soflySnowFlakeConfig;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Autowired
    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private static class WorkerIdRenewTask implements Runnable {

        private final Logger logger = LoggerFactory.getLogger(SnowflakeIdUtil.class);

        private SnowflakeIdUtil util = null;

        private final Object lock = new byte[0];

        public WorkerIdRenewTask(SnowflakeIdUtil util) {
            this.util = util;
        }

        @Override
        public void run() {
            while (!util.shutdownFlag) {
                synchronized (lock) {
                    Long waitTime = util.soflySnowFlakeConfig.getWorkerIdCacheTime() - 1800L;
                    if (waitTime < 0) {
                        waitTime = util.soflySnowFlakeConfig.getWorkerIdCacheTime() - 60L;
                    }
                    if (waitTime < 0) {
                        waitTime = util.soflySnowFlakeConfig.getWorkerIdCacheTime();
                    }
                    try {
                        lock.wait(waitTime * 1000);
                        util.renewWorkId();
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                        logger.warn(e.getMessage(), e);
                    }
                }
            }

        }
    }
}
