package com.soflyit.system.controller;

import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDeptUser;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.service.ISysDeptUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 用户部门Controller
 *
 * @author soflyit
 * @date 2022-06-08 17:10:03
 */
@RestController
@RequestMapping("/sysDeptUser")
@Api(tags = {"用户部门"})
public class SysDeptUserController extends BaseController {
    @Autowired
    private ISysDeptUserService sysDeptUserService;


    @RequiresPermissions("system:sysDeptUser:list")
    @GetMapping("/list")
    @ApiOperation("查询用户部门列表")
    public TableDataInfo list(SysDeptUser sysDeptUser) {
        startPage();
        List<SysDeptUser> list = sysDeptUserService.selectSysDeptUserList(sysDeptUser);
        return getDataTable(list);
    }

    @PostMapping("/getUsersByDeptIdList")
    @ApiOperation("根据部门数组获取用户列表")
    public R<List<SysUser>> getUsersByDeptIdList(@RequestBody @Validated @NotEmpty(message = "数组不能为空！") List<Long> deptIdList) {
        List<SysUser> list = sysDeptUserService.getUsersByDeptIdList(deptIdList);
        return R.ok(list);
    }


    @RequiresPermissions("system:sysDeptUser:export")
    @PostMapping("/export")
    @ApiOperation("导出用户部门列表")
    public void export(@ApiIgnore HttpServletResponse response, SysDeptUser sysDeptUser) {
        List<SysDeptUser> list = sysDeptUserService.selectSysDeptUserList(sysDeptUser);
        ExcelUtil<SysDeptUser> util = new ExcelUtil<SysDeptUser>(SysDeptUser.class);
        util.exportExcel(response, list, "用户部门数据");
    }


    @RequiresPermissions("system:sysDeptUser:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取用户部门详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysDeptUserService.selectSysDeptUserById(id));
    }


    @RequiresPermissions("system:sysDeptUser:add")
    @PostMapping
    @ApiOperation("新增用户部门")
    @ApiImplicitParam(name = "sysDeptUser", value = "详细信息", dataType = "SysDeptUser")
    public AjaxResult add(@RequestBody SysDeptUser sysDeptUser) {
        sysDeptUser.setCreateBy(SecurityUtils.getUserId());
        return toAjax(sysDeptUserService.insertSysDeptUser(sysDeptUser));
    }


    @RequiresPermissions("system:sysDeptUser:edit")
    @PutMapping
    @ApiOperation("修改用户部门")
    @ApiImplicitParam(name = "sysDeptUser", value = "根据ID修改详情", dataType = "SysDeptUser")
    public AjaxResult edit(@RequestBody SysDeptUser sysDeptUser) {
        sysDeptUser.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(sysDeptUserService.updateSysDeptUser(sysDeptUser));
    }


    @RequiresPermissions("system:sysDeptUser:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除用户部门")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysDeptUserService.deleteSysDeptUserByIds(ids));
    }
}
