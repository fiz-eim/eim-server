package com.soflyit.auth.controller;

import com.soflyit.auth.common.BspAuthConstant;
import com.soflyit.auth.config.AuthConfig;
import com.soflyit.auth.form.LoginBody;
import com.soflyit.auth.form.RegisterBody;
import com.soflyit.auth.service.SysLoginService;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.JwtUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.security.auth.AuthUtil;
import com.soflyit.common.security.service.TokenService;
import com.soflyit.common.security.utils.RSAUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * token 控制
 *
 * @author soflyit
 */
@Slf4j
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private AuthConfig authConfig;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody form) {

        LoginUser userInfo;
        try {
            String password = form.getPassword();
            if (StringUtils.equals(authConfig.getEncodePwdRule(), BspAuthConstant.LOGIN_PWD_ENCODE_RULE_RSA)) {
                password = RSAUtils.decrypt(form.getPassword());
            }
            userInfo = sysLoginService.login(form.getUsername(), password, form.getWechatCode(), false);

            return R.ok(tokenService.createToken(userInfo));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @GetMapping("changeDept/{deptId}")
    public R<?> changeDept(@PathVariable Long deptId) {
        LoginUser loginUser = tokenService.getLoginUser();
        sysLoginService.changeDept(loginUser, deptId);
        tokenService.refreshToken(loginUser);
        return R.ok();
    }

    @DeleteMapping("/logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        String wechatCode = request.getParameter("wechatCode");
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);

            AuthUtil.logoutByToken(token);

            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("/refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {

            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterBody registerBody) {

        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }


    @GetMapping("/ticket")
    public R<?> getTicket() {
        return R.ok(sysLoginService.getTicket());
    }

    @GetMapping("/tokenByTicket")
    public R<?> getTokenByTicket(String ticket) {
        String token = sysLoginService.getTokenByTicket(ticket);
        if (StringUtils.isBlank(token)) {
            return R.fail("票据已过期");
        }
        LoginUser user = tokenService.getLoginUser(token);
        tokenService.refreshToken(user);
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", token);
        rspMap.put("expires_in", 720L);
        return R.ok(rspMap);
    }

}
