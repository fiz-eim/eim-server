package com.soflyit.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.exception.ServiceException;
import com.soflyit.common.core.utils.SpringUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.datascope.annotation.DataScope;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.param.GetMenuRolesQuery;
import com.soflyit.system.domain.SysRoleDept;
import com.soflyit.system.domain.SysRoleMenu;
import com.soflyit.system.domain.SysUserRole;
import com.soflyit.system.mapper.*;
import com.soflyit.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author soflyit
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired
    private SysPostRoleMapper postRoleMapper;


    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }


    @Override
    public List<SysRole> selectRolesByUserId(Long appId, Long userId, Long deptId) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("appId", appId);
        if (deptId != null) {
            params.put("activeDeptId", deptId);
        }
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserInfo(params);
        List<SysRole> roles = selectRoleAll(appId);
        for (SysRole role : roles) {
            for (SysRole userRole : userRoles) {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue()) {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }


    @Override
    public Set<String> selectRolePermissionByUser(Long appId, SysUser user) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getUserId());
        params.put("appId", appId);
        params.put("activeDeptId", user.getDeptId());
        List<SysRole> perms = roleMapper.selectRolePermissionByUserInfo(params);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (StringUtils.isNotNull(perm)) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }


    @Override
    public List<SysRole> selectRoleAll(Long appId) {
        SysRole condition = new SysRole();
        condition.setAppId(appId);
        return SpringUtils.getAopProxy(this).selectRoleList(condition);
    }


    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }


    @Override
    public String checkRoleNameUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public String checkRoleKeyUnique(SysRole role) {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }


    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
            if (StringUtils.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色数据！");
            }
        }
    }


    @Override
    public int countPostRoleByRoleId(Long roleId) {
        return postRoleMapper.countPostRoleByRoleId(roleId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role) {

        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role) {

        roleMapper.updateRole(role);

        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }


    @Override
    public int updateRoleStatus(SysRole role) {
        SysRole sysRole = selectRoleById(role.getRoleId());
        if (countPostRoleByRoleId(role.getRoleId()) > 0) {
            throw new ServiceException(String.format("%1$s已分配,不能停用", sysRole.getRoleName()));
        }
        return roleMapper.updateRole(role);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role) {

        roleMapper.updateRole(role);

        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());

        return insertRoleDept(role);
    }


    public int insertRoleMenu(SysRole role) {
        int rows = 1;

        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (!list.isEmpty()) {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }


    public int insertRoleDept(SysRole role) {
        int rows = 1;

        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (!list.isEmpty()) {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId) {
        SysRole role = selectRoleById(roleId);
        if (countPostRoleByRoleId(roleId) > 0) {
            throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
        }

        roleMenuMapper.deleteRoleMenuByRoleId(roleId);

        roleDeptMapper.deleteRoleDeptByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countPostRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }

        roleMenuMapper.deleteRoleMenu(roleIds);

        roleDeptMapper.deleteRoleDept(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }


    @Override
    public int deleteAuthPost(SysPostRole sysPostRole) {
        return postRoleMapper.deletePostRoleInfo(sysPostRole);
    }


    @Override
    public int deleteAuthPosts(Long roleId, Long[] postIds) {
        return postRoleMapper.deletePostRoleInfos(roleId, postIds);
    }


    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {

        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }


    @Override
    public List<SysRole> getRoleList(SysRole sysRole) {
        return roleMapper.getRoleList(sysRole);
    }


    @Override
    public List<SysRole> getRoleByUserId(Long appId, Long userId) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("appId", appId);
        return roleMapper.selectRolePermissionByUserInfo(params);
    }


    @Override
    public PageInfo selectRolesByMenuId(GetMenuRolesQuery getMenuRolesQuery) {
        SysRole condition = new SysRole();
        Map<String, Object> params = condition.getParams();
        if (StringUtils.isNotEmpty(getMenuRolesQuery.getRoleName())) {
            condition.setRoleName(getMenuRolesQuery.getRoleName());
        }
        params.put("menuId", getMenuRolesQuery.getMenuId());
        params.put("appId", getMenuRolesQuery.getAppId());
        return new PageInfo(roleMapper.selectRolesByMenuId(condition));
    }

    @Override
    public List<SysRole> selectRoleListByIds(Long[] ids) {
        return roleMapper.selectRoleListByIds(ids);
    }
}
