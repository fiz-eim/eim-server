package com.soflyit.system.service;

import com.soflyit.auth.domain.vo.AppSecretData;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.system.api.domain.SysAuthApp;
import com.soflyit.system.api.domain.SysUser;

import java.util.List;
import java.util.Map;

/**
 * 应用Service接口
 *
 * @author soflyit
 * @date 2022-05-14
 */
public interface ISysAuthAppService {

    SysAuthApp selectSysAuthAppById(Long id);


    List<SysAuthApp> selectSysAuthAppList(SysAuthApp sysAuthApp);


    int insertSysAuthApp(SysAuthApp sysAuthApp);


    int updateSysAuthApp(SysAuthApp sysAuthApp);


    boolean deleteSysAuthAppByIds(Long[] ids);


    int deleteSysAuthAppById(Long id);


    SysAuthApp selectSysAuthAppByCode(String clientId);


    Map<Integer, AppSecretData> generateSecret(List<Integer> secretTypes);


    List<SysAuthApp> selectAppBaseInfo();


    AjaxResult<SysUser> addUser(SysUser user);
}
