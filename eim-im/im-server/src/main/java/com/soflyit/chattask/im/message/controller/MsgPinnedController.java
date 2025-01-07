package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgPinned;
import com.soflyit.chattask.im.message.service.IMsgPinnedService;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 固定消息Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/msgPinned")
@Slf4j
@Api(tags = {"固定消息"})
public class MsgPinnedController extends BaseController {
    @Autowired
    private IMsgPinnedService msgPinnedService;


    @RequiresPermissions("message:msgPinned:list")
    @GetMapping("/list")
    @ApiOperation("查询固定消息列表")
    public TableDataInfo list(MsgPinned msgPinned) {
        startPage();
        List<MsgPinned> list = msgPinnedService.selectMsgPinnedList(msgPinned);
        return getDataTable(list);
    }


    @RequiresPermissions("message:msgPinned:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取固定消息详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgPinnedService.selectMsgPinnedById(id));
    }


    @RequiresPermissions("message:msgPinned:add")
    @PostMapping
    @ApiOperation("新增固定消息")
    public AjaxResult add(@RequestBody MsgPinned msgPinned) {
        msgPinned.setCreateBy(SecurityUtils.getUserId());
        return toAjax(msgPinnedService.insertMsgPinned(msgPinned));
    }


    @RequiresPermissions("message:msgPinned:edit")
    @PutMapping
    @ApiOperation("修改固定消息")
    public AjaxResult edit(@RequestBody MsgPinned msgPinned) {
        msgPinned.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgPinnedService.updateMsgPinned(msgPinned));
    }


    @RequiresPermissions("message:msgPinned:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除固定消息")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(msgPinnedService.deleteMsgPinnedByIds(ids));
    }
}
