package com.soflyit.common.security.service;

import com.soflyit.common.core.constant.CacheConstants;
import com.soflyit.common.core.constant.Constants;
import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.utils.JwtUtils;
import com.soflyit.common.core.utils.ServletUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.ip.IpUtils;
import com.soflyit.common.core.utils.uuid.IdUtils;
import com.soflyit.common.redis.service.RedisService;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author soflyit
 */
@Slf4j
@Component
public class TokenService {
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    public final static String USER_NAME_TOKEN_KEY = "login_user:";

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;


    public Map<String, Object> createToken(LoginUser loginUser) {
        doKickOut(loginUser);
        String token = IdUtils.fastUUID();
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        refreshToken(loginUser);


        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);


        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);


        if (StringUtils.isNotEmpty(userName)) {
            String loginFailedKey = "user_login_failed:" + userName;
            if (redisService.hasKey(loginFailedKey)) {
                redisService.deleteObject(loginFailedKey);
            }
        }

        return rspMap;
    }


    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }


    public LoginUser getLoginUser(HttpServletRequest request) {

        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }


    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return user;
    }


    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }


    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            delLoginUserByUserKey(userkey);
        }
    }


    public void delLoginUserByUserKey(String userkey) {
        if (StringUtils.isNotEmpty(userkey)) {

            LoginUser loginUser = redisService.getCacheObject(getTokenKey(userkey));

            if (StringUtils.isNotNull(loginUser)) {
                SysUser currentUser = loginUser.getSysUser();
                Long userId = currentUser.getUserId();
                if (redisService.hasKey(Constants.BI_USER_AUTH_KEY + userId)) {
                    redisService.deleteObject(Constants.BI_USER_AUTH_KEY + userId);
                }
            }

            if (StringUtils.isNotNull(loginUser)) {
                SysUser currentUser = loginUser.getSysUser();
                Long userId = currentUser.getUserId();
                if (redisService.hasKey(Constants.DATA_GOVERNANCE_USER_AUTH_KEY + userId)) {
                    redisService.deleteObject(Constants.DATA_GOVERNANCE_USER_AUTH_KEY + userId);
                }
            }

            redisService.deleteObject(getTokenKey(userkey));
            if (loginUser != null) {
                Long userId = loginUser.getUserid();
                if (userId == null && loginUser.getSysUser() != null && loginUser.getSysUser().getUserId() != null) {
                    userId = loginUser.getSysUser().getUserId();
                }
                if (userId != null) {
                    redisService.deleteObject(USER_NAME_TOKEN_KEY + userId);
                }
            }
        }
    }


    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }


    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);

        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        Long userId = loginUser.getUserid();
        if (userId == null && loginUser.getSysUser() != null && loginUser.getSysUser().getUserId() != null) {
            userId = loginUser.getSysUser().getUserId();
        }
        if (userId != null) {
            redisService.setCacheObject(USER_NAME_TOKEN_KEY + userId, loginUser.getToken(), expireTime, TimeUnit.MINUTES);
        }
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }


    private void doKickOut(LoginUser loginUser) {
        if (loginUser == null || loginUser.getKickoutFlag() == null || !loginUser.getKickoutFlag()) {
            return;
        }
        loginUser.setKickoutFlag(null);
        Long userId = loginUser.getUserid();
        if (userId == null && loginUser.getSysUser() != null && loginUser.getSysUser().getUserId() != null) {
            userId = loginUser.getSysUser().getUserId();
        }
        if (userId != null) {
            String token = redisService.getCacheObject(USER_NAME_TOKEN_KEY + userId);
            delLoginUserByUserKey(token);
        }

    }

}
