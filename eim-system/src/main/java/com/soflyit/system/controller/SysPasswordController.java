package com.soflyit.system.controller;

import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysPassword;
import com.soflyit.system.service.ISysPasswordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 密码设置Controller
 *
 * @author soflyit
 * @date 2023-04-24 11:25:34
 */
@RestController
@RequestMapping("/sysPassword")
@Api(tags = {"密码设置"})
public class SysPasswordController extends BaseController {
    @Autowired
    private ISysPasswordService sysPasswordService;


    @RequiresPermissions("system:sysPassword:list")
    @GetMapping("/list")
    @ApiOperation("查询密码设置列表")
    public TableDataInfo list(SysPassword sysPassword) {
        startPage();
        List<SysPassword> list = sysPasswordService.selectSysPasswordList(sysPassword);
        return getDataTable(list);
    }


    @RequiresPermissions("system:sysPassword:export")
    @PostMapping("/export")
    @ApiOperation("导出密码设置列表")
    public void export(@ApiIgnore HttpServletResponse response, SysPassword sysPassword) {
        List<SysPassword> list = sysPasswordService.selectSysPasswordList(sysPassword);
        ExcelUtil<SysPassword> util = new ExcelUtil<SysPassword>(SysPassword.class);
        util.exportExcel(response, list, "密码设置数据");
    }


    @RequiresPermissions("system:sysPassword:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取密码设置详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysPasswordService.selectSysPasswordById(id));
    }


    @GetMapping(value = "/getSysPassword")
    @ApiOperation("获取密码设置详细信息")
    public AjaxResult getInfo() {
        return AjaxResult.success(sysPasswordService.selectSysPassword());
    }


    @RequiresPermissions("system:sysPassword:add")
    @PostMapping
    @ApiOperation("新增密码设置")
    @ApiImplicitParam(name = "sysPassword", value = "详细信息", dataType = "SysPassword")
    public AjaxResult add(@RequestBody SysPassword sysPassword) {
        sysPassword.setCreateBy(SecurityUtils.getUserId());
        return toAjax(sysPasswordService.insertSysPassword(sysPassword));
    }


    @RequiresPermissions("system:sysPassword:edit")
    @PutMapping
    @ApiOperation("修改密码设置")
    @ApiImplicitParam(name = "sysPassword", value = "根据ID修改详情", dataType = "SysPassword")
    public AjaxResult edit(@RequestBody SysPassword sysPassword) {
        if (sysPassword.getId() == -1L) {
            sysPassword.setId(null);
            sysPassword.setCreateBy(SecurityUtils.getUserId());
            return toAjax(sysPasswordService.insertSysPassword(sysPassword));
        }
        sysPassword.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(sysPasswordService.updateSysPassword(sysPassword));
    }


    @RequiresPermissions("system:sysPassword:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除密码设置")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysPasswordService.deleteSysPasswordByIds(ids));
    }
}
