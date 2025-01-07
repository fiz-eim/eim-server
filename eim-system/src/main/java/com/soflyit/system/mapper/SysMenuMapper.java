package com.soflyit.system.mapper;

import com.soflyit.system.api.domain.SysMenu;
import com.soflyit.system.domain.vo.SysMenuVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysMenuMapper {

    List<SysMenuVo> selectMenuList(SysMenu menu);


    List<String> selectMenuPerms(SysMenu menu);


    List<SysMenuVo> selectMenuListByUser(SysMenu menu);


    List<String> selectMenuPermsByUserId(SysMenu menu);


    List<SysMenuVo> selectMenuTreeAll(SysMenu condition);


    List<SysMenuVo> selectMenuTreeByUser(SysMenu condition);


    List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);


    SysMenuVo selectMenuById(Long menuId);


    int hasChildByMenuId(Long menuId);


    int insertMenu(SysMenu menu);


    int updateMenu(SysMenu menu);


    int deleteMenuById(Long menuId);


    SysMenuVo checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId, @Param("appId") Long appId);
}
