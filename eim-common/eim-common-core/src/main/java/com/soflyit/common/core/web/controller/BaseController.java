package com.soflyit.common.core.web.controller;

import com.github.pagehelper.PageInfo;
import com.soflyit.common.core.constant.HttpStatus;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.PageUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author soflyit
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }


    protected void startPage() {
        PageUtils.startPage();
    }



    protected Integer pageNum() {
        return PageUtils.pageNum();
    }


    protected Integer pageSize() {
        return PageUtils.pageSize();
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        rspData.setMsg("查询成功");
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }


    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }


    protected AjaxResult toAjax(int rows, String successMsg, String errorMsg) {
        return rows > 0 ? AjaxResult.success(successMsg) : AjaxResult.error(errorMsg);
    }


    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }


    public AjaxResult success() {
        return AjaxResult.success();
    }


    public AjaxResult error() {
        return AjaxResult.error();
    }


    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }


    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }
}
