package com.soflyit.chattask.dx.modular.share.controller;

import com.soflyit.chattask.dx.modular.share.domain.entity.FileShareEntity;
import com.soflyit.chattask.dx.modular.share.domain.param.FileShareAddParam;
import com.soflyit.chattask.dx.modular.share.domain.param.FileShareEditParam;
import com.soflyit.chattask.dx.modular.share.domain.param.FileShareQueryParam;
import com.soflyit.chattask.dx.modular.share.domain.vo.FileShareVO;
import com.soflyit.chattask.dx.modular.share.service.FileShareService;
import com.soflyit.common.core.utils.bean.BeanUtils;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;


/**
 * 文档系统级配置(KV)Controller
 *
 * @author soflyit
 * @date 2023-11-06 15:13:00
 */
@RestController
@RequestMapping("/share")
@Slf4j
@Api(tags = {"资源分享管理"})
public class FileShareController extends BaseController {

    @Resource
    private FileShareService fileShareService;


    @PostMapping("/page")
    @ApiOperation("资源分享-分页")
    public TableDataInfo page(@RequestBody FileShareQueryParam queryParam) {
        startPage();
        List<FileShareVO> list = fileShareService.list(queryParam);
        return getDataTable(list);
    }


    @PostMapping("/list")
    @ApiOperation("资源分享-列表")
    public AjaxResult list(@RequestBody FileShareQueryParam queryParam) {
        List<FileShareVO> list = fileShareService.list(queryParam);
        return AjaxResult.success(list);
    }


    @GetMapping(value = "/{id}")
    @ApiOperation("获取资源分享详细")
    @ApiImplicitParam(name = "configKey", value = "主键", dataType = "String", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(fileShareService.getById(id));
    }



    @PostMapping("/add")
    @ApiOperation("文档系统级配置-新增")
    @ApiImplicitParam(name = "dxSysConfig", value = "详细信息", dataType = "SysConfigParam")
    public AjaxResult add(@RequestBody FileShareAddParam addParam) {
        FileShareEntity fileShareEntity = BeanUtils.convertBean(addParam, FileShareEntity.class);
        fileShareEntity.setCreateBy(SecurityUtils.getUserId());
        return toAjax(fileShareService.save(fileShareEntity));

    }



    @PutMapping("/update")
    @ApiOperation("文档系统级配置-修改")
    @ApiImplicitParam(name = "editParam", value = "根据ID修改详情", dataType = "SysConfigEditParam")
    public AjaxResult edit(@RequestBody FileShareEditParam editParam) {
        FileShareEntity fileShareEntity = BeanUtils.convertBean(editParam, FileShareEntity.class);
        return toAjax(fileShareService.updateById(fileShareEntity));
    }



    @DeleteMapping("/{ids}")
    @ApiOperation("资源分享表-删除")
    @ApiImplicitParam(name = "configKeys", value = "根据IDS删除对应信息", dataType = "String", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(fileShareService.removeByIds(Arrays.asList(ids)));
    }
}
