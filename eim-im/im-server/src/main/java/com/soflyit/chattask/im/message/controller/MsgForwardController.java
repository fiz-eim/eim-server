package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgForward;
import com.soflyit.chattask.im.message.service.IMsgForwardService;
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
 * 消息转发Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/msgForword")
@Slf4j
@Api(tags = {"消息转发"})
public class MsgForwardController extends BaseController {
    @Autowired
    private IMsgForwardService msgForwordService;


    @RequiresPermissions("message:msgForword:list")
    @GetMapping("/list")
    @ApiOperation("查询消息转发列表")
    public TableDataInfo list(MsgForward msgForward) {
        startPage();
        List<MsgForward> list = msgForwordService.selectMsgForwardList(msgForward);
        return getDataTable(list);
    }


    @RequiresPermissions("message:msgForword:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息转发详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgForwordService.selectMsgForwardById(id));
    }


    @RequiresPermissions("message:msgForword:add")
    @PostMapping
    @ApiOperation("新增消息转发")
    public AjaxResult add(@RequestBody MsgForward msgForward) {
        msgForward.setCreateBy(SecurityUtils.getUserId());
        return toAjax(msgForwordService.insertMsgForward(msgForward));
    }


    @RequiresPermissions("message:msgForword:edit")
    @PutMapping
    @ApiOperation("修改消息转发")
    public AjaxResult edit(@RequestBody MsgForward msgForward) {
        msgForward.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgForwordService.updateMsgForward(msgForward));
    }


    @RequiresPermissions("message:msgForword:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除消息转发")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(msgForwordService.deleteMsgForwardByIds(ids));
    }
}
