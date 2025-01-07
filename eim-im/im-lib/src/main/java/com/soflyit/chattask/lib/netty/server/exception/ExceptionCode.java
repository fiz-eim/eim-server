package com.soflyit.chattask.lib.netty.server.exception;

import lombok.Getter;

/**
 * 一场吗
 */
@Getter
public enum ExceptionCode {

    MAX_CHANNEL_COUNT(101001, "通道连接失败，通道数量已达到系统上限"),
    SEND_FAILED_CACHE_NOT_EXIST(101002, "发送消息失败，通道管理器不存在"),
    SEND_FAILED_CHANNEL_NOT_EXIST(101003, "发送消息失败，通道不存在"),
    SYSTEM_UNKNOWN_ERROR(109999, "系统未知错误");

    ExceptionCode(int code, String messageCode) {
        this.code = code;
        this.messageCode = messageCode;
    }

    private int code = 0;

    private final String messageCode;


    public static ExceptionCode findByCode(int code) {
        ExceptionCode[] codes = ExceptionCode.values();
        for (ExceptionCode codeItem : codes) {
            if (codeItem.getCode() == code) {
                return codeItem;
            }
        }
        return null;
    }

    public String getMessage() {
        return this.messageCode;
    }

}
