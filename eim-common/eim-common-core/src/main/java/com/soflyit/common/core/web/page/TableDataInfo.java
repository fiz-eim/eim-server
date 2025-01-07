package com.soflyit.common.core.web.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author soflyit
 */
@ApiModel
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty("总记录数")
    private long total;


    @ApiModelProperty("列表数据")
    private List<T> rows;


    @ApiModelProperty("消息状态码")
    private int code;


    @ApiModelProperty("总记录数")
    private String msg;


    public TableDataInfo() {
    }


    public TableDataInfo(List<T> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
