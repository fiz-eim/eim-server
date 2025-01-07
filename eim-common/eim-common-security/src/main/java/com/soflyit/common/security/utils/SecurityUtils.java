package com.soflyit.common.security.utils;

import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.constant.TokenConstants;
import com.soflyit.common.core.context.SecurityContextHolder;
import com.soflyit.common.core.utils.ServletUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.system.api.model.LoginUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限获取工具类
 *
 * @author soflyit
 */
public class SecurityUtils {

    public static Long getUserId() {
        return SecurityContextHolder.getUserId();
    }


    public static String getUsername() {
        return SecurityContextHolder.getUserName();
    }


    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }


    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }


    public static String getToken() {
        return getToken(ServletUtils.getRequest());
    }


    public static String getToken(HttpServletRequest request) {

        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }


    public static String replaceTokenPrefix(String token) {

        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }


    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }


    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }


    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public static String getTenantCode() {
        return SecurityContextHolder.get(SecurityConstants.TENANT_CODE, String.class);
    }


    public static void setTenantCode(String tenantCode) {
        SecurityContextHolder.set(SecurityConstants.TENANT_CODE, tenantCode);
    }


    public static void main(String... args) {
        System.out.println(encryptPassword("123456"));
    }
}
