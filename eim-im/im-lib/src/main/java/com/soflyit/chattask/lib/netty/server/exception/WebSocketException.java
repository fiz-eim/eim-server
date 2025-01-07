package com.soflyit.chattask.lib.netty.server.exception;

import lombok.Data;

import static com.soflyit.chattask.lib.netty.server.exception.ExceptionCode.SYSTEM_UNKNOWN_ERROR;

/**
 * websocket 异常
 */
@Data
public class WebSocketException extends RuntimeException {
    private int code;

    private String message;

    public WebSocketException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public WebSocketException(String message) {
        super(message);
        this.code = SYSTEM_UNKNOWN_ERROR.getCode();
        this.message = message;
    }

    public WebSocketException(int code) {
        super();
        ExceptionCode exceptionCode = ExceptionCode.findByCode(code);
        this.code = code;
        if (exceptionCode == null) {
            exceptionCode = SYSTEM_UNKNOWN_ERROR;
        }
        this.message = exceptionCode.getMessage();
    }

}
