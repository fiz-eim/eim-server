package com.soflyit.chattask.dx.modular.file.controller;

import com.soflyit.chattask.dx.modular.file.domain.entity.FileTypeEntity;
import com.soflyit.chattask.dx.modular.file.service.FileTypeService;
import com.soflyit.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Package: com.soflyit.chattask.dx.modular.file.controller
 *
 * @Description:
 * @date: 2023/11/27 19:58
 * @author: dddgoal@163.com
 */
@RestController
@RequestMapping("/fileType")
@Slf4j
@Api(tags = {"文件类型"})
public class FileTypeController {
    @Resource
    private FileTypeService fileTypeService;

    @GetMapping("/list")
    @ApiOperation("查询文件类型列表")
    public AjaxResult list() {
        List<FileTypeEntity> list = fileTypeService.getAllFileType();
        return AjaxResult.success(list);
    }
}
