package com.soflyit.chattask.dx.modular.operate.controller;

import com.soflyit.chattask.dx.modular.operate.domain.entity.OperateRecordEntity;
import com.soflyit.chattask.dx.modular.operate.domain.param.OperateRecordAddParam;
import com.soflyit.chattask.dx.modular.operate.service.OperateRecordService;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文档库的变更记录Controller
 *
 * @author soflyit
 * @date 2023-11-06 14:53:26
 */
@RestController
@RequestMapping("/dxOperateRecord")
@Slf4j
@Api(tags = {"文档库的变更记录"})
public class OperateRecordController extends BaseController {
    @Setter(onMethod_ = {@Autowired})
    private OperateRecordService operateRecordService;


    @PostMapping("/list")
    @ApiOperation("查询文档库的变更记录列表")
    public TableDataInfo list(OperateRecordAddParam operateRecordParam) {
        startPage();
        List<OperateRecordEntity> list = operateRecordService.list();
        return getDataTable(list);
    }


    @PostMapping("/export")
    @ApiOperation("导出文档库的变更记录列表")
    public void export(@ApiIgnore HttpServletResponse response, OperateRecordAddParam operateRecordParam) {
        List<OperateRecordEntity> list = operateRecordService.list();
        ExcelUtil<OperateRecordEntity> util = new ExcelUtil<>(OperateRecordEntity.class);
        util.exportExcel(response, list, "文档库的变更记录数据");
    }



    @GetMapping(value = "/{operateId}")
    @ApiOperation("获取文档库的变更记录详细信息")
    @ApiImplicitParam(name = "operateId", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("operateId") Long operateId) {
        return AjaxResult.success(operateRecordService.getById(operateId));
    }


    @PostMapping
    @ApiOperation("新增文档库的变更记录")
    @ApiImplicitParam(name = "dxOperateRecord", value = "详细信息", dataType = "DxOperateRecord")
    public AjaxResult add(@RequestBody OperateRecordAddParam operateRecordParam) {

        return toAjax(operateRecordService.save(null));
    }


    @PutMapping
    @ApiOperation("修改文档库的变更记录")
    @ApiImplicitParam(name = "dxOperateRecord", value = "根据ID修改详情", dataType = "DxOperateRecord")
    public AjaxResult edit(@RequestBody OperateRecordAddParam operateRecordParam) {

        return toAjax(1);
    }


    @DeleteMapping("/{operateIds}")
    @ApiOperation("删除文档库的变更记录")
    @ApiImplicitParam(name = "operateIds", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] operateIds) {
        return toAjax(operateRecordService.removeById(operateIds));
    }
}
