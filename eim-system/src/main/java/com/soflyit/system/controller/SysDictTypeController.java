package com.soflyit.system.controller;

import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.InnerAuth;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDictType;
import com.soflyit.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/dict/type")
@Api(tags = {"数据字典类型管理"})
public class SysDictTypeController extends BaseController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @RequiresPermissions("system:dict:list")
    @ApiOperation(value = "根据条件分页查询字典类型")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @RequiresPermissions("system:dict:export")
    @ApiOperation(value = "导出字典类型")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }


    @RequiresPermissions("system:dict:query")
    @ApiOperation(value = "查询字典类型详细")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return AjaxResult.success(dictTypeService.selectDictTypeById(dictId));
    }


    @RequiresPermissions("system:dict:add")
    @ApiOperation(value = "新增字典类型")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return AjaxResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUserId());
        dict.setInherDict(false);
        return toAjax(dictTypeService.insertDictType(dict));
    }


    @InnerAuth
    @ApiOperation(value = "新增字典类型(内部)")
    @PostMapping("/add/inner")
    public AjaxResult innerAdd(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return AjaxResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUserId());
        dict.setInherDict(true);
        return toAjax(dictTypeService.insertDictTypeInner(dict));
    }


    @RequiresPermissions("system:dict:edit")
    @ApiOperation(value = "修改字典类型")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return AjaxResult.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(dictTypeService.updateDictType(dict));
    }


    @RequiresPermissions("system:dict:remove")
    @ApiOperation(value = "删除字典类型")
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }


    @RequiresPermissions("system:dict:remove")
    @ApiOperation(value = "刷新字典缓存")
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return AjaxResult.success();
    }


    @GetMapping("/optionselect")
    @ApiOperation(value = "获取字典选择框列表")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return AjaxResult.success(dictTypes);
    }


    @RequiresPermissions("system:dict:edit")
    @ApiOperation(value = "状态修改")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysDictType dict) {
        dict.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(dictTypeService.updateMenuStatus(dict));
    }
}
