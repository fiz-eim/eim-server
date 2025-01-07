package com.soflyit.chattask.dx.modular.resource.path.controller;

import com.soflyit.chattask.dx.modular.resource.path.domain.param.ResourcePathQueryParam;
import com.soflyit.chattask.dx.modular.resource.path.domain.vo.ResourcePathVO;
import com.soflyit.chattask.dx.modular.resource.path.service.ResourcePathService;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文档目录安全映射Controller
 *
 * @author soflyit
 * @date 2023-11-07 16:56:53
 */
@RestController
@RequestMapping("/dxResourcePath")
@Slf4j
@Api(tags = {"文档目录安全映射"})
public class ResourcePathController extends BaseController {
    @Resource
    private ResourcePathService resourcePathService;


    @RequiresPermissions("system:dxResourcePath:list")
    @PostMapping("/list")
    @ApiOperation("查询文档目录安全映射列表")
    public TableDataInfo list(ResourcePathQueryParam resourcePathParam) {
        List<ResourcePathVO> list = resourcePathService.list(resourcePathParam);
        return getDataTable(list);
    }


    @RequiresPermissions("system:dxResourcePath:export")
    @PostMapping("/export")
    @ApiOperation("导出文档目录安全映射列表")
    public void export(@ApiIgnore HttpServletResponse response, ResourcePathQueryParam resourcePathParam) {
        List<ResourcePathVO> list = resourcePathService.list(resourcePathParam);
        ExcelUtil<ResourcePathVO> util = new ExcelUtil<>(ResourcePathVO.class);
        util.exportExcel(response, list, "文档目录安全映射数据");
    }


    @RequiresPermissions("system:dxResourcePath:query")
    @GetMapping(value = "/{shareId}")
    @ApiOperation("获取文档目录安全映射详细信息")
    @ApiImplicitParam(name = "createdBy", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("shareId") Long shareId) {
        return AjaxResult.success(resourcePathService.getById(shareId));
    }


    @RequiresPermissions("system:dxResourcePath:add")
    @PostMapping
    @ApiOperation("新增文档目录安全映射")
    @ApiImplicitParam(name = "dxResourcePath", value = "详细信息", dataType = "DxResourcePath")
    public AjaxResult add(@RequestBody ResourcePathQueryParam resourcePathParam) {

        return toAjax(resourcePathService.save(null));
    }


    @RequiresPermissions("system:dxResourcePath:edit")
    @PutMapping
    @ApiOperation("修改文档目录安全映射")
    @ApiImplicitParam(name = "dxResourcePath", value = "根据ID修改详情", dataType = "DxResourcePath")
    public AjaxResult edit(@RequestBody ResourcePathQueryParam resourcePathParam) {

        return toAjax(resourcePathService.updateById(null));
    }


    @RequiresPermissions("system:dxResourcePath:remove")
    @DeleteMapping("/{createdBys}")
    @ApiOperation("删除文档目录安全映射")
    @ApiImplicitParam(name = "createdBys", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] createdBys) {
        return toAjax(resourcePathService.removeBatchByIds(null));
    }
}
