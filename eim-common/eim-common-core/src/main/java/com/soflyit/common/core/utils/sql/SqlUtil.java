package com.soflyit.common.core.utils.sql;

import com.soflyit.common.core.exception.UtilException;
import com.soflyit.common.core.utils.StringUtils;

/**
 * sql操作工具类
 *
 * @author soflyit
 */
public class SqlUtil {

    public static final String SQL_REGEX = "select |insert |delete |update |drop |count |exec |chr |mid |master |truncate |char |and |declare ";


    public static final String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";


    public static String escapeOrderBySql(String value) {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value)) {
            throw new UtilException("参数不符合规范，不能进行查询");
        }
        return value;
    }


    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }


    public static void filterKeyword(String value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                throw new UtilException("参数存在SQL注入风险");
            }
        }
    }
}
