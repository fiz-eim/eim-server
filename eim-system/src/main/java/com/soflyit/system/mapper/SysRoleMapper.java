package com.soflyit.system.mapper;

import com.soflyit.system.api.domain.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 角色表 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysRoleMapper {

    List<SysRole> selectRoleList(SysRole role);


    List<SysRole> selectRolePermissionByUserInfo(Map<String, Object> params);


    List<SysRole> selectRoleAll();


    SysRole selectRoleById(Long roleId);


    SysRole checkRoleNameUnique(String roleName);


    SysRole checkRoleKeyUnique(String roleKey);


    int updateRole(SysRole role);


    int insertRole(SysRole role);


    int deleteRoleById(Long roleId);


    int deleteRoleByIds(Long[] roleIds);


    List<SysRole> getRoleList(SysRole sysRole);


    List<SysRole> selectRolesByMenuId(SysRole condition);


    List<SysRole> unallocatedPostList(SysRole sysRole);


    List<SysRole> selectRoleListByIds(Long[] ids);
}
