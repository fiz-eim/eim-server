package com.soflyit.system.controller;

import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.service.ISysPostRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 岗位角色Controller
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
@RestController
@RequestMapping("/sysPostRole")
@Api(tags = {"岗位角色"})
public class SysPostRoleController extends BaseController {
    @Autowired
    private ISysPostRoleService sysPostRoleService;


    @RequiresPermissions("system:sysPostRole:list")
    @GetMapping("/list")
    @ApiOperation("查询岗位角色列表")
    public TableDataInfo list(SysPostRole sysPostRole) {
        startPage();
        List<SysPostRole> list = sysPostRoleService.selectSysPostRoleList(sysPostRole);
        return getDataTable(list);
    }


    @RequiresPermissions("system:sysPostRole:export")
    @PostMapping("/export")
    @ApiOperation("导出岗位角色列表")
    public void export(@ApiIgnore HttpServletResponse response, SysPostRole sysPostRole) {
        List<SysPostRole> list = sysPostRoleService.selectSysPostRoleList(sysPostRole);
        ExcelUtil<SysPostRole> util = new ExcelUtil<SysPostRole>(SysPostRole.class);
        util.exportExcel(response, list, "岗位角色数据");
    }


    @RequiresPermissions("system:sysPostRole:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取岗位角色详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysPostRoleService.selectSysPostRoleById(id));
    }


    @RequiresPermissions("system:sysPostRole:add")
    @PostMapping
    @ApiOperation("新增岗位角色")
    @ApiImplicitParam(name = "sysPostRole", value = "详细信息", dataType = "SysPostRole")
    public AjaxResult add(@RequestBody SysPostRole sysPostRole) {
        sysPostRole.setCreateBy(SecurityUtils.getUserId());
        return toAjax(sysPostRoleService.insertSysPostRole(sysPostRole));
    }


    @RequiresPermissions("system:sysPostRole:edit")
    @PutMapping
    @ApiOperation("修改岗位角色")
    @ApiImplicitParam(name = "sysPostRole", value = "根据ID修改详情", dataType = "SysPostRole")
    public AjaxResult edit(@RequestBody SysPostRole sysPostRole) {
        sysPostRole.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(sysPostRoleService.updateSysPostRole(sysPostRole));
    }


    @RequiresPermissions("system:sysPostRole:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除岗位角色")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysPostRoleService.deleteSysPostRoleByIds(ids));
    }
}
