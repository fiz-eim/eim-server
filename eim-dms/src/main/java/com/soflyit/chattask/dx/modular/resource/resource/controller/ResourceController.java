package com.soflyit.chattask.dx.modular.resource.resource.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.dx.modular.resource.resource.domain.ResourceAddParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.entity.ResourceEntity;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceDetailParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceEditParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.param.ResourceQueryParam;
import com.soflyit.chattask.dx.modular.resource.resource.domain.vo.ResourceVO;
import com.soflyit.chattask.dx.modular.resource.resource.service.ResourceService;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 文件资源Controller
 *
 * @author soflyit
 * @date 2023-11-07 16:56:57
 */
@RestController
@RequestMapping("/resource")
@Slf4j
@Api(tags = {"文件资源"})
public class ResourceController extends BaseController  {
    @Resource
    private ResourceService resourceService;


    @PostMapping("/list")
    @ApiOperation("查询文件资源列表")
    public AjaxResult list(ResourceQueryParam queryParam) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = resourceService.queryWrapper(queryParam);

        List<ResourceVO> list = resourceService.listDetail(queryWrapper);
        return AjaxResult.success(list);
    }


    @PostMapping("/export")
    @ApiOperation("导出文件资源列表")
    public void export(@ApiIgnore HttpServletResponse response, ResourceQueryParam queryParam) {
        List<ResourceEntity> list = null;//resourceService.selectDxResourceList(resourceParam);
        ExcelUtil<ResourceEntity> util = new ExcelUtil<>(ResourceEntity.class);
        util.exportExcel(response, list, "文件资源数据");
    }


    @GetMapping(value = "/{id}")
    @ApiOperation("资源管理-详情")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(resourceService.getById(id));
    }


    @PostMapping(value = "/detail")
    @ApiOperation("资源管理-文件/文件夹详情")
    public AjaxResult getResourceInfo(@RequestBody ResourceDetailParam resourceDetailParam) {
        return AjaxResult.success(resourceService.getResourceInfo(resourceDetailParam));
    }


    @GetMapping("/imageThumbnail/scale")
    public void getImageScale(@RequestParam("originId") Long originId,@RequestParam("scale") String scale, HttpServletResponse resp) {
        resourceService.getImageScale(originId,scale,resp);
    }



    @GetMapping("/imageThumbnail/width")
    public void getImageWidth(@RequestParam("originId") Long originId,@RequestParam("w") String w,@RequestParam("h") String h, HttpServletResponse resp) {
        resourceService.getImageScale(originId,w,h,resp);
    }






    @GetMapping("/download/{resourceId}")
    public void downloadFile(@PathVariable("resourceId") Long resourceId, HttpServletResponse response){
        resourceService.downloadFileOrFolder(resourceId, response, false);
    }



    @GetMapping("/download/noAuth/{resourceId}")
    public void downloadFileNoAuth(@PathVariable("resourceId") Long resourceId, HttpServletResponse response){
        resourceService.downloadFileOrFolder(resourceId, response, true);
    }


    @PostMapping
    @ApiOperation("资源管理-新增")
    @ApiImplicitParam(name = "addParam", value = "新增", dataType = "ResourceAddParam")
    public AjaxResult add(@RequestBody ResourceAddParam addParam) {

        return AjaxResult.success(resourceService.addResource(addParam));
    }


    @PutMapping
    @ApiOperation("修改文件资源")
    @ApiImplicitParam(name = "dxResource", value = "根据ID修改详情", dataType = "DxResource")
    public AjaxResult edit(@RequestBody ResourceEditParam editParam) {

        return toAjax(resourceService.updateById(null));
    }


    @DeleteMapping("/{ids}")
    @ApiOperation("删除文件资源")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(resourceService.removeByIds(Arrays.asList(ids)));
    }
}
