package com.soflyit.system.controller;

import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.domain.SysConfig;
import com.soflyit.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/config")
@Api(tags = {"参数配置"})
public class SysConfigController extends BaseController {
    @Autowired
    private ISysConfigService configService;


    @RequiresPermissions("system:config:list")
    @ApiOperation(value = "获取参数配置列表")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @RequiresPermissions("system:config:export")
    @ApiOperation(value = "参数配置导出")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }


    @GetMapping(value = "/{configId}")
    @ApiOperation(value = "根据参数编号获取参数配置")
    public AjaxResult getInfo(@PathVariable Long configId) {
        return AjaxResult.success(configService.selectConfigById(configId));
    }


    @GetMapping(value = "/configKey/{configKey}")
    @ApiOperation(value = "根据参数键名查询参数值")
    public AjaxResult getConfigKey(@PathVariable String configKey) {
        return AjaxResult.success(configService.selectConfigByKey(configKey));
    }


    @RequiresPermissions("system:config:add")
    @ApiOperation(value = "新增参数配置")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return AjaxResult.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUserId());
        return toAjax(configService.insertConfig(config));
    }


    @RequiresPermissions("system:config:edit")
    @ApiOperation(value = "修改参数配置")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return AjaxResult.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(configService.updateConfig(config));
    }


    @RequiresPermissions("system:config:remove")
    @DeleteMapping("/{configIds}")
    @ApiOperation(value = "批量删除参数配置")
    public AjaxResult remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }


    @RequiresPermissions("system:config:remove")
    @ApiOperation(value = "刷新参数缓存")
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        configService.resetConfigCache();
        return AjaxResult.success();
    }
}
