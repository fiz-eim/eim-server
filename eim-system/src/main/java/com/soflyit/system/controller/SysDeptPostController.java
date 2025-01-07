package com.soflyit.system.controller;

import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDeptPost;
import com.soflyit.system.service.ISysDeptPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 部门岗位Controller
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
@RestController
@RequestMapping("/sysDeptPost")
@Api(tags = {"部门岗位"})
public class SysDeptPostController extends BaseController {
    @Autowired
    private ISysDeptPostService sysDeptPostService;


    @RequiresPermissions("system:sysDeptPost:list")
    @GetMapping("/list")
    @ApiOperation("查询部门岗位列表")
    public TableDataInfo list(SysDeptPost sysDeptPost) {
        startPage();
        List<SysDeptPost> list = sysDeptPostService.selectSysDeptPostList(sysDeptPost);
        return getDataTable(list);
    }


    @RequiresPermissions("system:sysDeptPost:export")
    @PostMapping("/export")
    @ApiOperation("导出部门岗位列表")
    public void export(@ApiIgnore HttpServletResponse response, SysDeptPost sysDeptPost) {
        List<SysDeptPost> list = sysDeptPostService.selectSysDeptPostList(sysDeptPost);
        ExcelUtil<SysDeptPost> util = new ExcelUtil<SysDeptPost>(SysDeptPost.class);
        util.exportExcel(response, list, "部门岗位数据");
    }


    @RequiresPermissions("system:sysDeptPost:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取部门岗位详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysDeptPostService.selectSysDeptPostById(id));
    }


    @RequiresPermissions("system:sysDeptPost:add")
    @PostMapping
    @ApiOperation("新增部门岗位")
    @ApiImplicitParam(name = "sysDeptPost", value = "详细信息", dataType = "SysDeptPost")
    public AjaxResult add(@RequestBody SysDeptPost sysDeptPost) {
        sysDeptPost.setCreateBy(SecurityUtils.getUserId());
        return toAjax(sysDeptPostService.insertSysDeptPost(sysDeptPost));
    }


    @RequiresPermissions("system:sysDeptPost:edit")
    @PutMapping
    @ApiOperation("修改部门岗位")
    @ApiImplicitParam(name = "sysDeptPost", value = "根据ID修改详情", dataType = "SysDeptPost")
    public AjaxResult edit(@RequestBody SysDeptPost sysDeptPost) {
        sysDeptPost.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(sysDeptPostService.updateSysDeptPost(sysDeptPost));
    }


    @RequiresPermissions("system:sysDeptPost:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除部门岗位")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysDeptPostService.deleteSysDeptPostByIds(ids));
    }


    @PostMapping(value = "/getByDeptIds")
    @ApiOperation("根据部门Id获取相关岗位信息")
    @ApiImplicitParam(name = "deptIds", value = "根据部门Id获取岗位信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult getPostsByDeptIds(@RequestBody List<Long> deptIds) {
        return AjaxResult.success(sysDeptPostService.getPostsByDeptIds(deptIds));
    }

}
