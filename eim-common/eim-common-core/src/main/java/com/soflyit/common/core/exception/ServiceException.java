package com.soflyit.common.core.exception;

/**
 * 业务异常
 *
 * @author soflyit
 */
public final class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    private Integer code;


    private String message;


    private String detailMessage;


    public ServiceException() {
    }

    public ServiceException(String message) {
        this.message = message;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }
}
