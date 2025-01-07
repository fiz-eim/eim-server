package com.soflyit.common.core.utils.poi;

/**
 * Excel数据格式处理适配器
 *
 * @author soflyit
 */
public interface ExcelHandlerAdapter {

    Object format(Object value, String[] args);
}
