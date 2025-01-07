package com.soflyit.chattask.dx.modular.resource.attr.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.soflyit.chattask.dx.modular.resource.attr.domain.entity.ResourceAttrEntity;
import com.soflyit.chattask.dx.modular.resource.attr.service.ResourceAttrService;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文档属性扩展Controller
 *
 * @author soflyit
 * @date 2023-11-07 16:57:52
 */
@RestController
@RequestMapping("/dxResourceAttr")
@Slf4j
@Api(tags = {"文档属性扩展"})
public class ResourceAttrController extends BaseController {

    @Setter(onMethod_ = {@Autowired})
    private ResourceAttrService resourceAttrService;


    @GetMapping("/{resourceId}")
    @ApiOperation("查询文档属性扩展列表")
    public AjaxResult getInfo(@PathVariable Long resourceId) {
        LambdaQueryWrapper<ResourceAttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        ResourceAttrEntity info = resourceAttrService.getOne(queryWrapper.eq(ResourceAttrEntity::getResourceId, resourceId));
        return AjaxResult.success(info);
    }


}
