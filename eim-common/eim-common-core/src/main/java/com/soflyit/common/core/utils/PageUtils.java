package com.soflyit.common.core.utils;

import com.github.pagehelper.PageHelper;
import com.soflyit.common.core.utils.sql.SqlUtil;
import com.soflyit.common.core.web.page.PageDomain;
import com.soflyit.common.core.web.page.TableSupport;

/**
 * 分页工具类
 *
 * @author soflyit
 */
public class PageUtils extends PageHelper {

    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            Boolean reasonable = pageDomain.getReasonable();
            PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        }
    }


    public static void startPage(Integer dftPageNum, Integer dftPageSize) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();

        if (pageNum == null) {
            pageNum = dftPageNum;
        }

        if (pageSize == null) {
            pageSize = dftPageSize;
        }

        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            Boolean reasonable = pageDomain.getReasonable();
            PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        }
    }


    public static Integer pageNum() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return pageDomain.getPageNum();
    }


    public static Integer pageSize() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        return pageDomain.getPageSize();
    }
}
