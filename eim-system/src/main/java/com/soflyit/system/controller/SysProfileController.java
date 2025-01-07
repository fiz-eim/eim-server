package com.soflyit.system.controller;

import com.soflyit.common.core.constant.HttpStatus;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.redis.service.RedisService;
import com.soflyit.common.security.service.TokenService;
import com.soflyit.common.security.utils.RSAUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.RemoteOssService;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import com.soflyit.system.config.AvatarConfig;
import com.soflyit.system.service.ISysUserService;
import com.soflyit.system.service.impl.AvatarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 个人信息 业务处理
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/user/profile")
@Api(tags = {"个人信息管理"})
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteOssService remoteOssService;

    @Value("${spring.application.name}")
    private String bucketName;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AvatarService avatarService;

    @Autowired
    private AvatarConfig avatarConfig;


    @GetMapping
    @ApiOperation(value = "获取登录用户的个人信息")
    public AjaxResult profile() {
        String username = SecurityUtils.getUsername();
        SysUser condition = new SysUser();
        condition.setUserName(username);
        condition.getParams();
        SysUser user = userService.selectUserByUserName(condition);
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(username));
        ajax.put("postGroup", userService.selectUserPostGroup(username));
        ajax.put("deptGroup", userService.selectUserDeptGroup(username));
        return ajax;
    }


    @ApiOperation(value = "修改登录用户的个人信息")
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser sysUser = loginUser.getSysUser();
        user.setUserName(sysUser.getUserName());
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        if (userService.updateUserProfile(user) > 0) {

            loginUser.getSysUser().setNickName(user.getNickName());
            loginUser.getSysUser().setPhonenumber(user.getPhonenumber());
            loginUser.getSysUser().setEmail(user.getEmail());
            loginUser.getSysUser().setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }


    @ApiOperation(value = "修改密码")
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        try {
            oldPassword = RSAUtils.decrypt(oldPassword);
        } catch (Exception e) {
            return AjaxResult.error("旧密码格式不合法");
        }
        try {
            newPassword = RSAUtils.decrypt(newPassword);
        } catch (Exception e) {
            return AjaxResult.error("新密码格式不合法");
        }
        String username = SecurityUtils.getUsername();
        SysUser condition = new SysUser();
        condition.setUserName(username);
        condition.getParams();
        SysUser user = userService.selectUserByUserName(condition);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return AjaxResult.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }
        user.setPassword(SecurityUtils.encryptPassword(newPassword));

        user.setDefaultPwdFlag(Short.valueOf("0"));
        if (userService.changePwd(user) > 0) {

            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.getSysUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return AjaxResult.success();
        }
        return AjaxResult.error("修改密码异常，请联系管理员");
    }


    @ApiOperation(value = "修改用户头像")
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            AjaxResult<SysUser> result = avatarService.uploadUserAvatar(loginUser.getUserid(), file);
            if (result.getCode() == HttpStatus.SUCCESS) {
                String avatar = result.getData().getAvatar();
                result.putExtData("imgUrl", avatarConfig.getAccessUrl() + result.getData().getAvatar());
                loginUser.getSysUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
            }
            return result;
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
