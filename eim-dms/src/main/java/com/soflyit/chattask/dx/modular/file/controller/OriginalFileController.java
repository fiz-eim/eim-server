package com.soflyit.chattask.dx.modular.file.controller;

import com.soflyit.chattask.dx.modular.file.domain.entity.OriginalFileEntity;
import com.soflyit.chattask.dx.modular.file.domain.param.OriginalFileAddParam;
import com.soflyit.chattask.dx.modular.file.domain.param.OriginalFileEditParam;
import com.soflyit.chattask.dx.modular.file.domain.param.OriginalFileQueryParam;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 原始资源文件Controller
 *
 * @author jiaozhishang
 * @date 2023-11-10 14:43:36
 */
@RestController
@RequestMapping("/dxOriginalFile")
@Slf4j
@Api(tags = {"原始资源文件"})
public class OriginalFileController extends BaseController {


    @RequiresPermissions("system:dxOriginalFile:list")
    @GetMapping("/list")
    @ApiOperation("查询原始资源文件列表")
    public AjaxResult list(OriginalFileQueryParam queryParam) {
        List<OriginalFileEntity> list = null;// originalFileService.selectOriginalFileList(originalFileParam);
        return AjaxResult.success(list);
    }


    @RequiresPermissions("system:dxOriginalFile:export")
    @PostMapping("/export")
    @ApiOperation("导出原始资源文件列表")
    public void export(@ApiIgnore HttpServletResponse response, OriginalFileQueryParam queryParam) {
        List<OriginalFileEntity> list = null;//originalFileService.selectOriginalFileList(originalFileParam);
        ExcelUtil<OriginalFileEntity> util = new ExcelUtil<>(OriginalFileEntity.class);
        util.exportExcel(response, list, "原始资源文件数据");
    }


    @RequiresPermissions("system:dxOriginalFile:query")
    @GetMapping(value = "/{originalId}")
    @ApiOperation("获取原始资源文件详细信息")
    @ApiImplicitParam(name = "originalId", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("originalId") Long originalId) {
        return null;//AjaxResult.success(originalFileService.selectOriginalFileByOriginalId(originalId));
    }


    @RequiresPermissions("system:dxOriginalFile:add")
    @PostMapping
    @ApiOperation("新增原始资源文件")
    @ApiImplicitParam(name = "originalFile", value = "详细信息", dataType = "OriginalFile")
    public AjaxResult add(@RequestBody OriginalFileAddParam addParam) {

        return null;//toAjax(originalFileService.insertOriginalFile(originalFileParam));
    }


    @RequiresPermissions("system:dxOriginalFile:edit")
    @PutMapping
    @ApiOperation("修改原始资源文件")
    @ApiImplicitParam(name = "originalFile", value = "根据ID修改详情", dataType = "OriginalFile")
    public AjaxResult edit(@RequestBody OriginalFileEditParam editParam) {

        return null;//toAjax(originalFileService.updateOriginalFile(originalFileParam));
    }


    @RequiresPermissions("system:dxOriginalFile:remove")
    @DeleteMapping("/{originalIds}")
    @ApiOperation("删除原始资源文件")
    @ApiImplicitParam(name = "originalIds", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] originalIds) {
        return null;//toAjax(originalFileService.deleteOriginalFileByOriginalIds(originalIds));
    }
}
