package com.soflyit.auth.common;


import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务常量类
 *
 * @author Toney
 */
public class AuthConstants {


    public static final Integer SIGN_ALGORITHM_MD5 = 1;

    public static final String SIGNATURE_ALGORITHM_MD5 = "MD5";


    public static final Integer SIGN_ALGORITHM_RSA = 2;
    public static final String SIGNATURE_ALGORITHM_RSA = "RSA";


    public static final String SIGNATURE_ALGORITHM_SHA1_RSA = "SHA1WithRSA";


    public static final Integer SIGNATURE_RSA_DEFAULT_KEY_SIZE = 2048;


    public static final Map<Integer, String> SIGN_ALGORITHM_MAP = new HashMap<>();

    static {
        SIGN_ALGORITHM_MAP.put(SIGN_ALGORITHM_MD5, SIGNATURE_ALGORITHM_MD5);
        SIGN_ALGORITHM_MAP.put(SIGN_ALGORITHM_RSA, SIGNATURE_ALGORITHM_RSA);
    }

}
