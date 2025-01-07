package com.soflyit.auth.service;

import com.soflyit.common.core.constant.Constants;
import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.enums.UserStatus;
import com.soflyit.common.core.exception.ServiceException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.redis.service.RedisService;
import com.soflyit.common.security.utils.RSAUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.RemoteUserService;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录校验方法
 *
 * @author soflyit
 */
@Slf4j
@Component
public class SysLoginService {
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private RedisService redisService;

    private static final String TICKET_KEY = "ticket_";

    private final Long appId = -1L;


    public LoginUser login(String username, String password, String wechatCode, boolean sso) {

        if (StringUtils.isAnyBlank(username, password)) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }

        if (password.isEmpty()
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }

        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }


        R<LoginUser> userResult = remoteUserService.getUserInfo(username, null, null, SecurityConstants.INNER);


        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            R<List<LoginUser>> userList = remoteUserService.getUserInfoByPhone(username, null, SecurityConstants.INNER);
            if (R.FAIL == userList.getCode()) {
                String msg = userList.getMsg();
                if (msg.contains("密码")) {
                    msg = "登录用户不存在/密码错误";
                }
                throw new ServiceException(msg);
            }
            if (CollectionUtils.isNotEmpty(userList.getData())) {
                userResult = R.ok(userList.getData().get(0));
            }
        }

        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }
        if (!sso && !SecurityUtils.matchesPassword(password, user.getPassword())) {
            recordLoginInfor(username, Constants.LOGIN_FAIL, "用户密码错误");
            String msg = "登录用户不存在/密码错误";
            throw new ServiceException(msg);
        }

        recordLoginInfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    public void logout(String loginName) {
        recordLoginInfor(loginName, Constants.LOGOUT, "退出成功");
    }


    public void register(String username, String password) {

        if (StringUtils.isAnyBlank(username, password)) {
            throw new ServiceException("用户/密码必须填写");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("账户长度必须在2到20个字符之间");
        }
        if (password.isEmpty()
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new ServiceException("密码长度必须在5到20个字符之间");
        }


        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode()) {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLoginInfor(username, Constants.REGISTER, "注册成功");
    }


    public void recordLoginInfor(String username, String status, String message) {

    }

    public void changeDept(LoginUser loginUser, Long deptId) {

        R<LoginUser> userResult = remoteUserService.getUserInfo(loginUser.getSysUser().getUserName(), appId, deptId, SecurityConstants.INNER);
        SysUser user = userResult.getData().getSysUser();
        user.setDeptId(deptId);
        loginUser.setSysUser(userResult.getData().getSysUser());
    }

    public String getTicket() {
        String token = SecurityUtils.getToken();
        String ticket;
        try {
            ticket = RSAUtils.encrypt(token);
        } catch (Exception e) {
            ticket = UUID.randomUUID().toString().replace("-", "");
        }
        redisService.setCacheObject(TICKET_KEY + ticket, token, 300L, TimeUnit.SECONDS);
        return ticket;
    }


    public String getTokenByTicket(String ticket) {
        return redisService.getCacheObject(TICKET_KEY + ticket);
    }
}
