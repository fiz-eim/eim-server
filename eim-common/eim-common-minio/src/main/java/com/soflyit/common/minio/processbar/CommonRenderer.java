package com.soflyit.common.minio.processbar;

import com.soflyit.common.minio.processbar.listener.ProgressListener;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.DefaultProgressBarRenderer;
import me.tongfei.progressbar.ProgressBarStyle;
import me.tongfei.progressbar.ProgressState;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;

/**
 * 通用渲染器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-23 18:05
 */
@Slf4j
public class CommonRenderer extends DefaultProgressBarRenderer {

    private final ProgressListener processListener;

    public CommonRenderer(ProgressBarStyle style, String unitName, long unitSize, boolean isSpeedShown, DecimalFormat speedFormat,
                          ChronoUnit speedUnit, boolean isEtaShown, Function<ProgressState, Optional<Duration>> eta,
                          ProgressListener processListener) {
        super(style, unitName, unitSize, isSpeedShown, speedFormat, speedUnit, isEtaShown, eta);
        this.processListener = processListener;
    }

    @Override
    public String render(ProgressState progress, int maxLength) {
        doNotifyListener(progress);


        return super.render(progress, maxLength);
    }

    private void doNotifyListener(ProgressState progress) {
        if (processListener != null) {
            int percentage = calcPercentage(progress);
            processListener.onUpdate(progress, percentage);
        }
    }

    private int calcPercentage(ProgressState progress) {
        long current = progress.getCurrent();
        long max = progress.getMax();
        int percentage = 0;
        if (max > 0) {
            if (current < 0) {
                current = 0;
            }
            if (current > max) {
                current = max;
            }
            try {
                percentage = Math.toIntExact(current * 100 / max);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        } else {
            percentage = 100;
        }

        return percentage;
    }
}
