package com.soflyit.chattask.lib.im.fs.task;

import com.soflyit.chattask.lib.im.fs.service.FailedFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 失败文件重试<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-10 14:38
 */
@Component
@Slf4j
public class FiledFileRetryTask {

    private FailedFileService failedFileService;

    @Scheduled(fixedRate = 60000)
    public void retry() {
        if (failedFileService == null) {
            log.warn("执行重试失败，找不到失败文件处理服务");
        }
        try {
            failedFileService.processFiledFile();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    @Autowired(required = false)
    public void setFailedFileService(FailedFileService failedFileService) {
        this.failedFileService = failedFileService;
    }
}
