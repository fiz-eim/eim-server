package com.soflyit.system.controller;

import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.InnerAuth;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDictData;
import com.soflyit.system.service.ISysDictDataService;
import com.soflyit.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/dict/data")
@Api(tags = {"数据字典项管理"})
public class SysDictDataController extends BaseController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "根据条件分页查询字典数据")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }


    @RequiresPermissions("system:dict:list")
    @PostMapping("/list")
    @ApiOperation(value = "根据条件分页查询字典数据")
    public TableDataInfo postList(@RequestBody SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @InnerAuth
    @PostMapping("/list/inner")
    @ApiOperation(value = "根据条件分页查询字典数据(内部)")
    public TableDataInfo innerPostList(@RequestBody SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }


    @RequiresPermissions("system:dict:export")
    @ApiOperation(value = "导出数据字典项信息")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        util.exportExcel(response, list, "字典数据");
    }


    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    @ApiOperation(value = "根据字典数据ID查询信息")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }


    @GetMapping(value = "/type/{dictType}")
    @ApiOperation(value = "根据字典类型查询字典数据信息")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }


    @PostMapping(value = "/type/{dictType}")
    @ApiOperation(value = "根据字典类型查询字典数据信息")
    public AjaxResult postDictType(@PathVariable("dictType") String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }


    @RequiresPermissions("system:dict:add")
    @ApiOperation(value = "新增字典数据")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setInherDict(false);
        dict.setCreateBy(SecurityUtils.getUserId());
        int flag = dictDataService.insertDictData(dict);
        if (flag < 0) {
            if (flag == -1) {
                return AjaxResult.error("字典键值已重复");
            } else {
                return AjaxResult.error("字典标签已重复");
            }
        } else {
            return toAjax(flag);
        }
    }


    @InnerAuth
    @ApiOperation(value = "新增字典数据(内部)")
    @PostMapping("/add/inner")
    public AjaxResult innerAdd(@Validated @RequestBody SysDictData dict) {
        dict.setInherDict(true);
        dict.setCreateBy(SecurityUtils.getUserId());
        int flag = dictDataService.insertDictDataInner(dict);
        if (flag < 0) {
            if (flag == -1) {
                return AjaxResult.error("字典键值已重复");
            } else {
                return AjaxResult.error("字典标签已重复");
            }
        } else {
            return toAjax(flag);
        }
    }


    @RequiresPermissions("system:dict:edit")
    @ApiOperation(value = "修改保存字典数据")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUserId());
        int flag = dictDataService.updateDictData(dict);
        if (flag < 0) {
            if (flag == -1) {
                return AjaxResult.error("字典键值已重复");
            } else {
                return AjaxResult.error("字典标签已重复");
            }
        } else {
            return toAjax(flag);
        }
    }


    @RequiresPermissions("system:dict:remove")
    @ApiOperation(value = "删除保存字典数据")
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }

    @ApiOperation(value = "获取字典标签值")
    @GetMapping("/getDictLabel/{dictType},{dictValue}")
    public R getDictName(@PathVariable String dictType, @PathVariable String dictValue) {
        return R.ok(dictDataService.selectDictLabel(dictType, dictValue));
    }

}
