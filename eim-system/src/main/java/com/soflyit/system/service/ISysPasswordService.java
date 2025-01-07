package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysPassword;

import java.util.List;

/**
 * 密码设置Service接口
 *
 * @author soflyit
 * @date 2023-04-24 11:25:34
 */
public interface ISysPasswordService {

    SysPassword selectSysPasswordById(Long id);


    List<SysPassword> selectSysPasswordList(SysPassword sysPassword);


    int insertSysPassword(SysPassword sysPassword);


    int updateSysPassword(SysPassword sysPassword);


    int deleteSysPasswordByIds(Long[] ids);


    int deleteSysPasswordById(Long id);


    SysPassword selectSysPassword();

}
