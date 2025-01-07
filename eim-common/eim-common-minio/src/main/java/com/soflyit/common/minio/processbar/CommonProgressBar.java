package com.soflyit.common.minio.processbar;

import com.soflyit.common.minio.processbar.builder.CommonProgressBarBuilder;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarConsumer;
import me.tongfei.progressbar.ProgressBarRenderer;

import java.time.Duration;

/**
 * 通用进度条<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-23 17:53
 */
public class CommonProgressBar extends ProgressBar {
    public CommonProgressBar(String task, long initialMax) {
        super(task, initialMax);
    }

    @SuppressWarnings("deprecation")
    public CommonProgressBar(
            String task,
            long initialMax,
            int updateIntervalMillis,
            boolean continuousUpdate,
            boolean clearDisplayOnFinish,
            long processed,
            Duration elapsed,
            ProgressBarRenderer renderer,
            ProgressBarConsumer consumer
    ) {
        super(task, initialMax, updateIntervalMillis, continuousUpdate,
                clearDisplayOnFinish, processed, elapsed, renderer, consumer);
    }

    public static CommonProgressBarBuilder builder() {
        return new CommonProgressBarBuilder();
    }


}
