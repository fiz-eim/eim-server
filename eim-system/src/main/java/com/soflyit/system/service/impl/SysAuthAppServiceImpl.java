package com.soflyit.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.auth.domain.vo.AppSecretData;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.exception.ServiceException;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.utils.RSAUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysAuthApp;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.mapper.SysAuthAppMapper;
import com.soflyit.system.service.ISysAuthAppService;
import com.soflyit.system.service.ISysUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用Service业务层处理
 *
 * @author soflyit
 * @date 2022-05-14
 */
@Service
public class SysAuthAppServiceImpl extends ServiceImpl<SysAuthAppMapper, SysAuthApp> implements ISysAuthAppService {
    @Autowired
    private SysAuthAppMapper sysAuthAppMapper;

    @Autowired
    private SecretService secretService;

    @Autowired
    private ISysUserService userService;



    @Override
    public SysAuthApp selectSysAuthAppById(Long id) {
        return sysAuthAppMapper.selectSysAuthAppById(id);
    }


    @Override
    public List<SysAuthApp> selectSysAuthAppList(SysAuthApp sysAuthApp) {
        return sysAuthAppMapper.selectSysAuthAppList(sysAuthApp);
    }


    @Override
    public int insertSysAuthApp(SysAuthApp sysAuthApp) {
        sysAuthApp.setCreateTime(DateUtils.getNowDate());
        doCheckAppExist(sysAuthApp);
        return sysAuthAppMapper.insertSysAuthApp(sysAuthApp);
    }


    @Override
    public int updateSysAuthApp(SysAuthApp sysAuthApp) {
        sysAuthApp.setUpdateTime(DateUtils.getNowDate());
        doCheckAppExist(sysAuthApp);
        return sysAuthAppMapper.updateSysAuthApp(sysAuthApp);
    }


    @Override
    public boolean deleteSysAuthAppByIds(Long[] ids) {
        for (Long id : ids) {
            int i = sysAuthAppMapper.deleteSysAuthAppById(id);
            return i > 0;
        }
        return false;
    }


    @Override
    public int deleteSysAuthAppById(Long id) {
        return sysAuthAppMapper.deleteSysAuthAppById(id);
    }


    @Override
    public SysAuthApp selectSysAuthAppByCode(String clientId) {
        SysAuthApp condition = new SysAuthApp();
        condition.setClientId(clientId);
        List<SysAuthApp> apps = sysAuthAppMapper.selectSysAuthAppList(condition);
        if (CollectionUtils.isEmpty(apps)) {
            return null;
        } else if (apps.size() > 1) {
            throw new ServiceException("数据异常，查询应用失败");
        }
        return apps.get(0);
    }


    @Override
    public Map<Integer, AppSecretData> generateSecret(List<Integer> secretTypes) {
        Map<Integer, AppSecretData> secretDataMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(secretTypes)) {
            secretTypes.forEach(secretType -> {
                AppSecretData data = secretService.generateSecret(secretType);
                secretDataMap.put(secretType, data);
            });
        }
        return secretDataMap;
    }


    @Override
    public List<SysAuthApp> selectAppBaseInfo() {
        SysAuthApp condition = new SysAuthApp();
        List<SysAuthApp> result = selectSysAuthAppList(condition);
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(item -> {
                item.setAuthMode(null);
                item.setAuthProtocol(null);
                item.setCreateBy(null);
                item.setCreateTime(null);
                item.setUpdateBy(null);
                item.setUpdateTime(null);
                item.setLoginUrl(null);
                item.setLogoutType(null);
                item.setLogoutUrl(null);
                item.setRevision(null);
                item.setSecretType(null);
                item.setSecretPrivate(null);
                item.setSecretPub(null);
                item.setSsoType(null);
            });
        }
        return result;
    }


    @Override
    public AjaxResult<SysUser> addUser(SysUser user) {

        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        try {
            user.setPassword(SecurityUtils.encryptPassword(RSAUtils.decrypt(user.getPassword())));
        } catch (Exception e) {
            return AjaxResult.error("密码格式不合法");
        }
        userService.insertUser(user);
        user.setPassword(null);
        return AjaxResult.success(user);
    }

    private void doCheckAppExist(SysAuthApp app) {
        SysAuthApp condition = new SysAuthApp();

        condition.setClientId(app.getClientId());
        List<SysAuthApp> configs = selectSysAuthAppList(condition);
        if (CollectionUtils.isNotEmpty(configs)) {
            String msg = null;
            if (app.getId() == null) {
                msg = "应用注册失败，应用[" + app.getClientId() + "]已存在";
            } else if (configs.size() > 1) {
                msg = "修改应用失败，应用[" + app.getClientId() + "]已存在";
            } else if (configs.size() == 1 && !app.getId().equals(configs.get(0).getId())) {
                msg = "修改应用失败，应用[" + app.getClientId() + "]已存在";
            }
            if (StringUtils.isNotEmpty(msg)) {
                throw new BaseException(msg);
            }
        }
    }
}
