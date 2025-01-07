package com.soflyit.auth.common;

/**
 * 统一鉴权常量
 *
 * @author Toney
 */
public class BspAuthConstant {


    public static final String ACCESS_TOKEN_STORE_PREFIX = "bsp:accessToken:";


    public static final String REFRESH_TOKEN_STORE_PREFIX = "bsp:refreshToken:";


    public static final String AUTHORIZATION_CODE_STORE_PREFIX = "bsp:authCode:";


    public static final String APP_USER_TOKEN_KEY_PREFIX = "bsp:app-user:";


    public static final String GRANT_TYPE_PASSWORD = "password";

    public static final String GRANT_TYPE_AUTH_CODE = "authorization_code";

    public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials ";

    public static final String GRANT_TYPE_PHONE_VCODE = "phone_vcode";

    public static final String GRANT_TYPE_PHONE_PASSWORD = "phone_password";


    public static final String LOGIN_PWD_ENCODE_RULE_RSA = "rsa";

    public static final String LOGIN_PWD_ENCODE_RULE_NONE = "none";

}
