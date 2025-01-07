package com.soflyit.system.mapper;

import com.soflyit.system.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysRoleMenuMapper {

    int checkMenuExistRole(Long menuId);


    int deleteRoleMenuByRoleId(Long roleId);


    int deleteRoleMenu(Long[] ids);


    int batchRoleMenu(List<SysRoleMenu> roleMenuList);


    int deleteAuthRole(SysRoleMenu sysRoleMenu);


    int deleteAuthRoles(@Param("menuId") Long menuId, @Param("roleIds") Long[] roleIds);


    int insertAuthRoles(List<SysRoleMenu> list);


    int deleteRoleMunus(List<SysRoleMenu> list);
}
