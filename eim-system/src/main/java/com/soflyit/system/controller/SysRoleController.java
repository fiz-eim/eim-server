package com.soflyit.system.controller;

import com.github.pagehelper.PageInfo;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysPost;
import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.param.GetMenuRolesQuery;
import com.soflyit.system.service.ISysPostService;
import com.soflyit.system.service.ISysRoleService;
import com.soflyit.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/role")
@Api(tags = {"角色管理"})
public class SysRoleController extends BaseController {
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysPostService postService;

    @RequiresPermissions("system:role:list")
    @ApiOperation(value = "获取角色列表")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role) {
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @RequiresPermissions("system:role:export")
    @ApiOperation(value = "导出角色数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role) {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }


    @RequiresPermissions("system:role:query")
    @ApiOperation(value = "根据角色编号获取详细信息")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }


    @ApiOperation(value = "新增角色")
    @RequiresPermissions("system:role:add")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，权限字符已存在");
        }
        role.setCreateBy(SecurityUtils.getUserId());
        return toAjax(roleService.insertRole(role));

    }


    @ApiOperation(value = "修改角色")
    @RequiresPermissions("system:role:edit")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(roleService.updateRole(role));
    }


    @ApiOperation(value = "修改角色数据权限")
    @RequiresPermissions("system:role:edit")
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }


    @ApiOperation(value = "修改角色状态")
    @RequiresPermissions("system:role:edit")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(roleService.updateRoleStatus(role));
    }


    @ApiOperation(value = "批量删除角色状态")
    @RequiresPermissions("system:role:remove")
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }


    @ApiOperation(value = "获取角色选择框列表")
    @RequiresPermissions("system:role:query")
    @GetMapping("/optionselect")
    public AjaxResult optionselect(@RequestParam(value = "appId", required = false) Long appId) {
        return AjaxResult.success(roleService.selectRoleAll(appId));
    }


    @RequiresPermissions("system:role:list")
    @GetMapping("/authUser/allocatedList")
    @ApiOperation(value = "查询已分配用户角色列表")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }


    @ApiOperation(value = "查询未分配角色岗位列表")
    @RequiresPermissions("system:role:list")
    @GetMapping("/authPost/unallocatedPostList")
    public TableDataInfo unallocatedPostList(SysPost sysPost) {
        startPage();
        List<SysPost> list = postService.unallocatedPostList(sysPost);
        return getDataTable(list);
    }


    @ApiOperation(value = "批量授权角色岗位")
    @RequiresPermissions("system:role:edit")
    @PutMapping("/authPost/insertAll")
    public AjaxResult insertAuthPostAll(Long roleId, Long[] postIds, Long appId) {
        return toAjax(postService.insertAuthPosts(roleId, postIds, appId));
    }


    @ApiOperation(value = "取消授权岗位")
    @RequiresPermissions("system:role:edit")
    @PutMapping("/authPost/cancel")
    public AjaxResult cancelAuthPost(@RequestBody SysPostRole sysPostRole) {
        return toAjax(roleService.deleteAuthPost(sysPostRole));
    }


    @ApiOperation(value = "批量取消授权岗位")
    @RequiresPermissions("system:role:edit")
    @PutMapping("/authPost/cancelAll")
    public AjaxResult cancelAuthPostAll(Long roleId, Long[] postIds) {
        return toAjax(roleService.deleteAuthPosts(roleId, postIds));
    }


    @ApiOperation(value = "批量选择用户授权")
    @RequiresPermissions("system:role:edit")
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }



    @ApiOperation(value = "获取菜单角色")
    @GetMapping("/getMenuRoles")
    public R<PageInfo> getMenuRoles(GetMenuRolesQuery getMenuRolesQuery) {
        startPage();
        PageInfo list = roleService.selectRolesByMenuId(getMenuRolesQuery);
        return R.ok(list, "查询成功");
    }

    @ApiOperation(value = "获取角色列表根据角色Ids")
    @GetMapping("/getRoleListByIds/{ids}")
    public AjaxResult list(@PathVariable Long[] ids) {
        List<SysRole> list = roleService.selectRoleListByIds(ids);
        return AjaxResult.success(list);
    }
}
