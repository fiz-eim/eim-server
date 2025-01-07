package com.soflyit.common.core.exception.base;

/**
 * 基础异常
 *
 * @author soflyit
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    private final String module;


    private final String code;


    private final Object[] args;


    private final String defaultMessage;

    public BaseException(String module, String code, Object[] args, String defaultMessage) {
        super(defaultMessage);
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
