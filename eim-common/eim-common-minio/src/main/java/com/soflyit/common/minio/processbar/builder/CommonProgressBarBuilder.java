package com.soflyit.common.minio.processbar.builder;

import com.soflyit.common.minio.processbar.CommonConsumer;
import com.soflyit.common.minio.processbar.CommonProgressBar;
import com.soflyit.common.minio.processbar.CommonRenderer;
import com.soflyit.common.minio.processbar.listener.ProgressListener;
import me.tongfei.progressbar.*;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 进度条构造器<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-23 18:19
 */
public class CommonProgressBarBuilder extends ProgressBarBuilder {

    private String task = "";
    private long initialMax = -1;
    private int updateIntervalMillis = 1000;
    private boolean continuousUpdate = false;
    private ProgressBarStyle style = ProgressBarStyle.COLORFUL_UNICODE_BLOCK;
    private ProgressBarConsumer consumer = null;
    private boolean clearDisplayOnFinish = false;
    private String unitName = "";
    private long unitSize = 1;
    private boolean showSpeed = false;
    private DecimalFormat speedFormat;
    private ChronoUnit speedUnit = ChronoUnit.SECONDS;
    private long processed = 0;
    private Duration elapsed = Duration.ZERO;
    private int maxRenderedLength = -1;

    private ProgressListener listener = null;

    public CommonProgressBarBuilder() {
    }

    public ProgressBarBuilder setTaskName(String task) {
        this.task = task;
        return this;
    }

    boolean initialMaxIsSet() {
        return this.initialMax != -1;
    }

    public ProgressBarBuilder setInitialMax(long initialMax) {
        this.initialMax = initialMax;
        return this;
    }

    public ProgressBarBuilder setStyle(ProgressBarStyle style) {
        this.style = style;
        return this;
    }

    public ProgressBarBuilder setUpdateIntervalMillis(int updateIntervalMillis) {
        this.updateIntervalMillis = updateIntervalMillis;
        return this;
    }

    public ProgressBarBuilder continuousUpdate() {
        this.continuousUpdate = true;
        return this;
    }

    public ProgressBarBuilder setConsumer(ProgressBarConsumer consumer) {
        this.consumer = consumer;
        return this;
    }

    public ProgressBarBuilder clearDisplayOnFinish() {
        this.clearDisplayOnFinish = true;
        return this;
    }

    public ProgressBarBuilder setUnit(String unitName, long unitSize) {
        this.unitName = unitName;
        this.unitSize = unitSize;
        return this;
    }

    public ProgressBarBuilder setMaxRenderedLength(int maxRenderedLength) {
        this.maxRenderedLength = maxRenderedLength;
        return this;
    }

    public ProgressBarBuilder showSpeed() {
        return showSpeed(new DecimalFormat("#.0"));
    }

    public ProgressBarBuilder showSpeed(DecimalFormat speedFormat) {
        this.showSpeed = true;
        this.speedFormat = speedFormat;
        return this;
    }

    public ProgressBarBuilder setSpeedUnit(ChronoUnit speedUnit) {
        this.speedUnit = speedUnit;
        return this;
    }


    public ProgressBarBuilder startsFrom(long processed, Duration elapsed) {
        this.processed = processed;
        this.elapsed = elapsed;
        return this;
    }

    public ProgressBarBuilder setProgressListener(ProgressListener progressListener) {
        this.listener = progressListener;
        return this;
    }

    public ProgressBar build() {
        return new CommonProgressBar(
                task,
                initialMax,
                updateIntervalMillis,
                continuousUpdate,
                clearDisplayOnFinish,
                processed,
                elapsed,
                buildCommonRender(
                        style, unitName, unitSize,
                        showSpeed, speedFormat, speedUnit),
                consumer == null ? buildCommonConsumer() : consumer
        );
    }

    private ProgressBarRenderer buildCommonRender(ProgressBarStyle style, String unitName, long unitSize, boolean showSpeed, DecimalFormat speedFormat, ChronoUnit speedUnit) {
        return new CommonRenderer(style, unitName, unitSize, showSpeed, speedFormat, speedUnit,
                Boolean.FALSE, (progress) -> Optional.empty(), listener);
    }

    private ProgressBarConsumer buildCommonConsumer() {
        Consumer<String> consumer = new CommonConsumer();
        return new DelegatingProgressBarConsumer(consumer, 100);
    }
}
