package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysPostRole;

import java.util.List;

/**
 * 岗位角色Service接口
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
public interface ISysPostRoleService {

    SysPostRole selectSysPostRoleById(Long id);


    List<SysPostRole> selectSysPostRoleList(SysPostRole sysPostRole);


    int insertSysPostRole(SysPostRole sysPostRole);


    int updateSysPostRole(SysPostRole sysPostRole);


    int deleteSysPostRoleByIds(Long[] ids);


    int deleteSysPostRoleById(Long id);
}
