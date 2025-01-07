package com.soflyit.system.service.impl;

import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.service.ISysMenuService;
import com.soflyit.system.service.ISysPermissionService;
import com.soflyit.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SysPermissionServiceImpl implements ISysPermissionService {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;


    @Override
    public Set<String> getRolePermission(Long appId, Long userId, Long deptId) {
        Set<String> roles = new HashSet<String>();

        if (SysUser.isAdmin(userId)) {
            roles.add("admin");
        } else {
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.setDeptId(deptId);
            roles.addAll(roleService.selectRolePermissionByUser(appId, user));
        }
        return roles;
    }


    @Override
    public Set<String> getMenuPermission(Long appId, Long userId, Long deptId) {
        Set<String> perms = new HashSet<String>();

        if (SysUser.isAdmin(userId)) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(appId, userId, deptId));
        }
        return perms;
    }
}
