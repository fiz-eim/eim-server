package com.soflyit.common.core.exception;

/**
 * 全局异常
 *
 * @author soflyit
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;


    private String message;


    private String detailMessage;


    public GlobalException() {
    }

    public GlobalException(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public GlobalException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public GlobalException setMessage(String message) {
        this.message = message;
        return this;
    }
}
