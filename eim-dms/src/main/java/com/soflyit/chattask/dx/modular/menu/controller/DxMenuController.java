package com.soflyit.chattask.dx.modular.menu.controller;

import com.soflyit.chattask.dx.modular.menu.domain.param.DxMenuAddParam;
import com.soflyit.chattask.dx.modular.menu.domain.param.DxMenuUpdateParam;
import com.soflyit.chattask.dx.modular.menu.service.DxMenuService;
import com.soflyit.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Package: com.soflyit.chattask.dx.modular.dx
 *
 * @Description:
 * @date: 2023/12/18 9:54
 * @author: dddgoal@163.com
 */

@RestController
@RequestMapping("/dx/menu")
@Slf4j
@Api(tags = {"文档顶级菜单处理"})
public class DxMenuController  {
    @Resource
    private DxMenuService dxMenuService;


    @GetMapping(value = "/{id}")
    @ApiOperation("文档顶级菜单-详情")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(dxMenuService.getInfo(id));
    }



    @PostMapping
    @ApiOperation("文档顶级菜单-新增")
    @ApiImplicitParam(name = "addParam", value = "新增", dataType = "ResourceAddParam")
    public AjaxResult add(@RequestBody DxMenuAddParam addParam) {

        return AjaxResult.success(dxMenuService.addTopMenu(addParam));
    }


    @PutMapping
    @ApiOperation("修改文档顶级菜单")
    @ApiImplicitParam(name = "editParam", value = "根据ID修改详情", dataType = "DxMenuUpdateParam")
    public AjaxResult edit(@RequestBody DxMenuUpdateParam editParam) {

        return AjaxResult.success(dxMenuService.updateTopMenu(editParam));
    }


    @DeleteMapping("/{ids}")
    @ApiOperation("删除文档顶级菜单")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {

        return AjaxResult.success(dxMenuService.deleteTopMenu(Arrays.asList(ids)));
    }
}
