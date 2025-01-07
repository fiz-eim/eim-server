package com.soflyit.common.minio.processbar.listener;

import me.tongfei.progressbar.ProgressState;

/**
 * 进度监听<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-23 17:02
 */
public interface ProgressListener {


    void onUpdate(ProgressState progressState, int percent);

}
