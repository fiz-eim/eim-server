package com.soflyit.common.core.constant;

/**
 * 通用常量信息
 *
 * @author soflyit
 */
public class Constants {

    public static final String UTF8 = "UTF-8";


    public static final String GBK = "GBK";


    public static final String LOOKUP_RMI = "rmi:";


    public static final String LOOKUP_LDAP = "ldap:";


    public static final String LOOKUP_LDAPS = "ldaps:";


    public static final String HTTP = "http://";


    public static final String HTTPS = "https://";


    public static final String BASE_URL = "${baseUrl}";


    public static final Integer SUCCESS = 200;


    public static final Integer FAIL = 500;


    public static final String LOGIN_SUCCESS_STATUS = "0";


    public static final String LOGIN_FAIL_STATUS = "1";


    public static final String LOGIN_SUCCESS = "Success";


    public static final String LOGOUT = "Logout";


    public static final String REGISTER = "Register";


    public static final String LOGIN_FAIL = "Error";


    public static final String PAGE_NUM = "pageNum";


    public static final String PAGE_SIZE = "pageSize";


    public static final String ORDER_BY_COLUMN = "orderByColumn";


    public static final String IS_ASC = "isAsc";


    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";


    public static final long CAPTCHA_EXPIRATION = 2;



    public static final String SYS_CONFIG_KEY = "sys_config:";


    public static final String SYS_DICT_KEY = "sys_dict:";


    public static final String BI_USER_AUTH_KEY = "bi_user_auth:";


    public static final String BI_CACHE_KEY = "bi_cache:";


    public static final String DATA_GOVERNANCE_USER_AUTH_KEY = "data_governance_user_auth:";


    public static final String RESOURCE_PREFIX = "/profile";


    public static final String[] JOB_WHITELIST_STR = {"com.soflyit"};


    public static final String INVALID = "invalid";


    public static final String[] JOB_ERROR_STR = {"java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "com.soflyit.common.core.utils.file"};
}
