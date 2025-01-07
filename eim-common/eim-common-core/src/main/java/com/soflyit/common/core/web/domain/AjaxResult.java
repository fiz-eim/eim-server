package com.soflyit.common.core.web.domain;

import com.soflyit.common.core.constant.HttpStatus;
import com.soflyit.common.core.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作消息提醒
 *
 * @author soflyit
 */
@ApiModel
@Data
@Slf4j
public class AjaxResult<T> {
    private static final long serialVersionUID = 1L;


    public static final String CODE_TAG = "code";


    public static final String MSG_TAG = "msg";


    public static final String DATA_TAG = "data";


    private Map<String, Object> extData = new HashMap<>();


    @ApiModelProperty(value = "状态码", example = "200")
    private int code;

    @ApiModelProperty("返回消息")
    private String msg;

    @ApiModelProperty("数据")
    private T data;


    public AjaxResult() {
    }


    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public AjaxResult(int code, String msg, T data) {
        this(code, msg);
        this.data = data;
    }


    public AjaxResult put(String key, Object value) {
        if (StringUtils.equals(key, DATA_TAG)) {
            this.data = (T) value;
        } else {
            putExtData(key, value);
        }
        return this;
    }


    public AjaxResult putExtData(String key, Object value) {
        extData.put(key, value);
        return this;
    }


    public Object get(String key) {
        Object result = null;
        if (StringUtils.equals(key, CODE_TAG)) {
            result = code;
        } else if (StringUtils.equals(key, MSG_TAG)) {
            result = msg;
        } else if (StringUtils.equals(key, DATA_TAG)) {
            result = data;
        }
        if (result == null) {
            result = getExtDataValue(key);
        }
        return result;
    }


    public Object getExtDataValue(String key) {
        return extData.get(key);
    }


    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }


    public static <T> AjaxResult success(T data) {
        return AjaxResult.success("操作成功", data);
    }


    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }


    public static <T> AjaxResult success(String msg, T data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }


    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }


    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }


    public static <T> AjaxResult error(String msg, T data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }


    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }
}
