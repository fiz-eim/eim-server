package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysMenu;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.domain.SysRoleMenu;
import com.soflyit.system.domain.vo.RouterVo;
import com.soflyit.system.domain.vo.SysMenuVo;
import com.soflyit.system.domain.vo.TreeSelect;

import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author soflyit
 */
public interface ISysMenuService {

    @Deprecated
    List<SysMenuVo> selectMenuList(Long userId);


    List<SysMenuVo> selectMenuList(SysMenu menu, SysUser user);


    List<SysMenuVo> selectMenuList(SysMenu menu);


    Set<String> selectMenuPermsByUserId(Long appId, Long userId, Long deptId);


    List<SysMenu> selectMenuTreeByUser(Long appId, SysUser user);


    List<Long> selectMenuListByRoleId(Long roleId);


    List<RouterVo> buildMenus(List<SysMenu> menus);


    List<SysMenu> buildMenuTree(List<SysMenu> menus);


    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);


    SysMenuVo selectMenuById(Long menuId);


    boolean hasChildByMenuId(Long menuId);


    boolean checkMenuExistRole(Long menuId);


    int insertMenu(SysMenu menu);


    int updateMenu(SysMenu menu);


    int deleteMenuById(Long menuId);


    String checkMenuNameUnique(SysMenu menu);


    int updateMenuStatus(SysMenu menu);


    int deleteAuthRole(SysRoleMenu sysRoleMenu);


    int deleteAuthRoles(Long menuId, Long[] roleIds);


    List<SysRole> unallocatedPostList(SysRole sysRole);


    int insertAuthRoles(Long[] menuIds, Long[] roleIds);

    List<RouterVo> buildMenusV3(List<SysMenu> menus);
}
