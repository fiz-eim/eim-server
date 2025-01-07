package com.soflyit.system.service;

import com.github.pagehelper.PageInfo;
import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.param.GetMenuRolesQuery;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author soflyit
 */
public interface ISysRoleService {

    List<SysRole> selectRoleList(SysRole role);


    List<SysRole> selectRolesByUserId(Long appId, Long userId, Long deptId);


    Set<String> selectRolePermissionByUser(Long appId, SysUser user);


    List<SysRole> selectRoleAll(Long appId);


    SysRole selectRoleById(Long roleId);


    String checkRoleNameUnique(SysRole role);


    String checkRoleKeyUnique(SysRole role);


    void checkRoleAllowed(SysRole role);


    void checkRoleDataScope(Long roleId);


    int countPostRoleByRoleId(Long roleId);


    int insertRole(SysRole role);


    int updateRole(SysRole role);


    int updateRoleStatus(SysRole role);


    int authDataScope(SysRole role);


    int deleteRoleById(Long roleId);


    int deleteRoleByIds(Long[] roleIds);


    int deleteAuthPost(SysPostRole sysPostRole);


    int deleteAuthPosts(Long roleId, Long[] postIds);


    int insertAuthUsers(Long roleId, Long[] userIds);



    List<SysRole> getRoleList(SysRole sysRole);


    List<SysRole> getRoleByUserId(Long appId, Long userId);


    PageInfo selectRolesByMenuId(GetMenuRolesQuery getMenuRolesQuery);


    List<SysRole> selectRoleListByIds(Long[] ids);
}
