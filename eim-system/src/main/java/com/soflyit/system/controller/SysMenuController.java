package com.soflyit.system.controller;

import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.InnerAuth;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysMenu;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import com.soflyit.system.config.SystemConfig;
import com.soflyit.system.domain.SysRoleMenu;
import com.soflyit.system.domain.vo.SysMenuVo;
import com.soflyit.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/menu")
@Api(tags = {"菜单管理"})
public class SysMenuController extends BaseController {
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SystemConfig systemConfig;


    @InnerAuth
    @ApiOperation(value = "获取菜单列表")
    @GetMapping("/info")
    public R<List<SysMenuVo>> info(SysMenu menu) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        if (menu.getAppId() == null) {
            menu.setAppId(systemConfig.getAppId());
        }
        List<SysMenuVo> menus = menuService.selectMenuList(menu, user);
        return R.ok(menus);
    }


    @RequiresPermissions("system:menu:list")
    @ApiOperation(value = "获取菜单列表")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        Long userId = SecurityUtils.getUserId();
        if (menu.getAppId() == null) {
            menu.setAppId(systemConfig.getAppId());
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        List<SysMenuVo> menus = menuService.selectMenuList(menu, user);

        return AjaxResult.success(menus);
    }


    @RequiresPermissions("system:menu:query")
    @ApiOperation(value = "根据菜单编号获取详细信息")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }


    @GetMapping("/treeselect")
    @ApiOperation(value = "根据菜单编号获取详细信息")
    public AjaxResult treeselect(SysMenu menu) {
        if (menu.getAppId() == null) {
            menu.setAppId(systemConfig.getAppId());
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        List<SysMenu> menus = BeanUtils.convertList(menuService.selectMenuList(menu, user), SysMenu.class);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }


    @GetMapping(value = "/roleMenuTreeselect")
    @ApiOperation(value = "获取对应角色菜单列表树")
    public AjaxResult roleMenuTreeselect(@RequestParam("roleId") Long roleId, @RequestParam("appId") Long appId) {
        SysMenu menu = new SysMenu();
        if (appId == null) {
            appId = systemConfig.getAppId();
        }
        menu.setAppId(appId);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        List<SysMenu> menus = BeanUtils.convertList(menuService.selectMenuList(menu, user), SysMenu.class);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }


    @RequiresPermissions("system:menu:add")
    @ApiOperation(value = "新增菜单")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (menu.getAppId() == null) {
            menu.setAppId(systemConfig.getAppId());
        }
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return AjaxResult.error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(SecurityUtils.getUserId());
        menu.setCreateUser(SecurityUtils.getLoginUser().getSysUser().getNickName());
        return toAjax(menuService.insertMenu(menu));
    }


    @RequiresPermissions("system:menu:add")
    @ApiOperation(value = "新增菜单")
    @PostMapping("/add")
    public R<SysMenu> addMenu(@RequestBody SysMenu sysMenu) {
        if (sysMenu.getAppId() == null) {
            sysMenu.setAppId(systemConfig.getAppId());
        }
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(sysMenu))) {
            return R.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame()) && !StringUtils.ishttp(sysMenu.getPath())) {
            return R.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        sysMenu.setCreateBy(SecurityUtils.getUserId());
        int result = menuService.insertMenu(sysMenu);
        return result > 0 ? R.ok(sysMenu) : R.fail(sysMenu, "菜单添加失败");
    }


    @RequiresPermissions("system:menu:edit")
    @ApiOperation(value = "修改菜单")
    @PostMapping("/update")
    public R<SysMenu> updateMenu(@RequestBody SysMenu sysMenu) {
        if (sysMenu.getAppId() == null) {
            sysMenu.setAppId(systemConfig.getAppId());
        }
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(sysMenu))) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame()) && !StringUtils.ishttp(sysMenu.getPath())) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (sysMenu.getMenuId().equals(sysMenu.getParentId())) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        sysMenu.setUpdateBy(SecurityUtils.getUserId());
        int result = menuService.updateMenu(sysMenu);
        return result > 0 ? R.ok(sysMenu) : R.fail(sysMenu, "修改菜单失败");
    }


    @RequiresPermissions("system:menu:edit")
    @ApiOperation(value = "修改菜单")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (menu.getAppId() == null) {
            menu.setAppId(systemConfig.getAppId());
        }
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return AjaxResult.error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(menuService.updateMenu(menu));
    }


    @RequiresPermissions("system:menu:remove")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return AjaxResult.error("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return AjaxResult.error("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }


    @RequiresPermissions("system:menu:remove")
    @ApiOperation(value = "删除菜单")
    @PostMapping("/delete")
    public R deleteMenu(@RequestParam("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return R.fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return R.fail("菜单已分配,不允许删除");
        }
        SysMenu menu = menuService.selectMenuById(menuId);
        boolean existFlag = menu != null;
        int result = 0;
        if (existFlag) {
            result = menuService.deleteMenuById(menuId);
        }
        return !existFlag || result > 0 ? R.ok(menu) : R.fail("删除菜单失败");
    }


    @GetMapping("getRouters")
    @ApiOperation(value = "构建菜单路由信息")
    public AjaxResult getRouters(@RequestParam(value = "appId", required = false) Long appId) {
        LoginUser user = SecurityUtils.getLoginUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUser(appId, user.getSysUser());
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    @RequiresPermissions("system:role:edit")
    @ApiOperation(value = "修改菜单状态")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysMenu menu) {
        menu.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(menuService.updateMenuStatus(menu));
    }


    @ApiOperation(value = "取消授权菜单角色")
    @RequiresPermissions("system:menu:edit")
    @PutMapping("/authRole/cancel")
    public AjaxResult cancelAuthRole(@RequestBody SysRoleMenu sysRoleMenu) {
        return toAjax(menuService.deleteAuthRole(sysRoleMenu));
    }


    @ApiOperation(value = "批量取消授权菜单角色")
    @RequiresPermissions("system:menu:edit")
    @PutMapping("/authRole/cancelAll")
    public AjaxResult cancelAuthRoleAll(Long menuId, Long[] roleIds) {
        return toAjax(menuService.deleteAuthRoles(menuId, roleIds));
    }


    @ApiOperation(value = "查询菜单未授权角色列表")
    @RequiresPermissions("system:menu:list")
    @GetMapping("/authRole/unallocatedRoleList")
    public TableDataInfo unallocatedRoleList(SysRole sysRole) {
        startPage();
        List<SysRole> list = menuService.unallocatedPostList(sysRole);
        return getDataTable(list);
    }


    @ApiOperation(value = "批量授权菜单角色")
    @RequiresPermissions("system:menu:edit")
    @PutMapping("/authRole/insertAll")
    public AjaxResult insertAuthRoleAll(Long[] menuIds, Long[] roleIds) {
        return toAjax(menuService.insertAuthRoles(menuIds, roleIds));
    }
}
