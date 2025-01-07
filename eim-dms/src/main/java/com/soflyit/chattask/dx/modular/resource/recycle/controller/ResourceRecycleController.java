package com.soflyit.chattask.dx.modular.resource.recycle.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.dx.modular.resource.recycle.domain.param.ResourceRecycleQueryParam;
import com.soflyit.chattask.dx.modular.resource.recycle.entity.ResourceRecycleEntity;
import com.soflyit.chattask.dx.modular.resource.recycle.service.ResourceRecycleService;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 回收站 Controller
 *
 * @author soflyit
 * @date 2023-11-07 16:56:53
 */
@RestController
@RequestMapping("/resourceRecycle")
@Slf4j
@Api(tags = {"回收站"})
public class ResourceRecycleController extends BaseController {
    @Resource
    private ResourceRecycleService resourceRecycleService;


    @PostMapping("/page")
    @ApiOperation("查询回收站列表")
    public TableDataInfo page(ResourceRecycleQueryParam queryParam) {
        LambdaQueryWrapper<ResourceRecycleEntity> queryWrapper = new LambdaQueryWrapper<>();

        List<ResourceRecycleEntity> list = resourceRecycleService.list(queryWrapper);
        return getDataTable(list);
    }

    @GetMapping("/list")
    @ApiOperation("查询回收站列表")
    public AjaxResult list(ResourceRecycleQueryParam queryParam) {
        LambdaQueryWrapper<ResourceRecycleEntity> queryWrapper = new LambdaQueryWrapper<>();

        List<ResourceRecycleEntity> list = resourceRecycleService.list(queryWrapper);
        return AjaxResult.success(list);
    }

    @GetMapping("/resourceRestore/{resourceId}")
    @ApiOperation("回收站还原")
    public AjaxResult resourceRestore(@PathVariable("resourceId") Long resourceId) {
        return AjaxResult.success(resourceRecycleService.resourceRestore(resourceId));
    }

    @GetMapping("/removeForever/{resourceId}")
    @ApiOperation("回收站永久删除资源")
    public AjaxResult removeForever(@PathVariable("resourceId") Long resourceId) {
        return AjaxResult.success(resourceRecycleService.removeForever(resourceId));
    }

}
