package com.soflyit.system.domain.vo;

import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.SysUser;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 用户信息，包括：用户信息、角色信息、部门信息、权限信息、应用信息、路由菜单信息<br>
 * 详细说明
 *
 * @author Toney
 * @date 2024-01-05 18:13
 */
@Data
public class SysUserInfoVo {

    private SysUser user;

    private Set<String> roles;

    private Set<String> permissions;

    private List<RouterVo> routers;

    private List<SysDept> depts;

}
