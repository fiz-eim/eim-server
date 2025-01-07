package com.soflyit.common.core.web.upload;

import org.apache.commons.fileupload.ProgressListener;
import org.springframework.beans.factory.annotation.Value;

/**
 * 进度更新监听器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-24 11:11:17
 */
public abstract class UploadProgressListener implements ProgressListener {

    private final ThreadLocal<ProgressData> progressDataLocal = new InheritableThreadLocal<>();
    private final ThreadLocal<Long> lastTimeLocal = new InheritableThreadLocal<>();

    @Value("${soflyit.file.upload.update-interval-millis:100}")
    private int notifyLimit = 100;

    public UploadProgressListener() {
    }

    public abstract void onProgress(ProgressData data);

    public void update(long pBytesRead, long pContentLength, int pItems) {
        ProgressData progressData = progressDataLocal.get();
        if (progressData == null) {
            progressData = new ProgressData();
        }
        progressData.setProgressItems(pItems);
        progressData.setProgressBytesRead(pBytesRead);
        progressData.setProgressContentLength(pContentLength);

        Long lastTime = lastTimeLocal.get();
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || pBytesRead >= pContentLength || currentTime - lastTime > notifyLimit) {
            try {
                onProgress(progressData.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            lastTimeLocal.set(currentTime);
        }
    }

    public ProgressData getProgressData() {
        ProgressData progressData = progressDataLocal.get();
        if (progressData == null) {
            return null;
        }
        try {
            return progressData.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProgressData(ProgressData progressData) {
        progressDataLocal.set(progressData);
    }
}
