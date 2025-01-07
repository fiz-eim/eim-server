package com.soflyit.common.core.web.upload;

import lombok.Data;

/**
 * 进度数据<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-24 11:13
 */
@Data
public class ProgressData implements Cloneable {


    private String token;


    private String fileUploadKey;


    private long progressBytesRead;


    private long progressContentLength;


    private int progressItems;


    private int totalItems;


    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    protected ProgressData clone() throws CloneNotSupportedException {
        ProgressData data = new ProgressData();
        data.setTotalItems(totalItems);
        data.setProgressBytesRead(progressBytesRead);
        data.setProgressItems(progressItems);
        data.setProgressContentLength(progressContentLength);
        data.setToken(token);
        data.setFileUploadKey(fileUploadKey);
        return data;
    }
}
