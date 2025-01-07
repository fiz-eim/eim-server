package com.soflyit.chattask.dx.modular.cofig.controller;

import com.soflyit.chattask.dx.modular.cofig.domain.entity.SysConfigEntity;
import com.soflyit.chattask.dx.modular.cofig.domain.param.SysConfigAddParam;
import com.soflyit.chattask.dx.modular.cofig.domain.param.SysConfigEditParam;
import com.soflyit.chattask.dx.modular.cofig.domain.param.SysConfigQueryParam;
import com.soflyit.chattask.dx.modular.cofig.service.SysConfigService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 文档系统级配置(KV)Controller
 *
 * @author soflyit
 * @date 2023-11-06 15:13:00
 */
@RestController
@RequestMapping("/config")
@Slf4j
@Api(tags = {"文档系统配置管理"})
public class SysConfigController extends BaseController {

    @Resource
    private SysConfigService sysConfigService;


    @PostMapping("/page")
    @ApiOperation("文档系统配置-列表")
    public TableDataInfo page(SysConfigQueryParam queryParam) {
        startPage();
        List<SysConfigEntity> list = sysConfigService.list();
        return getDataTable(list);
    }


    @PostMapping("/list")
    @ApiOperation("文档系统配置-列表")
    public AjaxResult list(@RequestBody SysConfigQueryParam queryParam) {
        List<SysConfigEntity> list = sysConfigService.list(queryParam);
        return AjaxResult.success(list);
    }


    @PostMapping("/export")
    @ApiOperation("导出文档系统级配置(KV)列表")
    public void export(@ApiIgnore HttpServletResponse response, SysConfigQueryParam queryParam) {
        List<SysConfigEntity> list = sysConfigService.list(queryParam);
        ExcelUtil<SysConfigEntity> util = new ExcelUtil<>(SysConfigEntity.class);
        util.exportExcel(response, list, "文档系统配置管理数据");
    }




    @GetMapping(value = "/{id}")
    @ApiOperation("获取文档系统级配置(KV)详细")
    @ApiImplicitParam(name = "configKey", value = "主键", dataType = "String", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(sysConfigService.getById(id));
    }



    @PostMapping("/add")
    @ApiOperation("文档系统级配置-新增")
    @ApiImplicitParam(name = "dxSysConfig", value = "详细信息", dataType = "SysConfigParam")
    public AjaxResult add(@RequestBody SysConfigAddParam addParam) {
        SysConfigEntity sysConfigEntity = BeanUtils.convertBean(addParam, SysConfigEntity.class);
        sysConfigEntity.setCreateBy(SecurityUtils.getUserId());

        return toAjax(sysConfigService.save(sysConfigEntity));

    }



    @PutMapping
    @ApiOperation("文档系统级配置-修改")
    @ApiImplicitParam(name = "dxSysConfig", value = "根据ID修改详情", dataType = "SysConfigParam")
    public AjaxResult edit(@RequestBody SysConfigEditParam editParam) {
        SysConfigEntity sysConfigEntity = BeanUtils.convertBean(editParam, SysConfigEntity.class);
        sysConfigEntity.setCreateBy(SecurityUtils.getUserId());
        return toAjax(sysConfigService.updateById(sysConfigEntity));
    }



    @DeleteMapping("/{ids}")
    @ApiOperation("文档系统配置表-删除")
    @ApiImplicitParam(name = "configKeys", value = "根据IDS删除对应信息", dataType = "String", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(sysConfigService.removeByIds(Arrays.asList(ids)));
    }
}
