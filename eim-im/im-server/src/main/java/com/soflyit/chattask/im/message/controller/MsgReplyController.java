package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgReply;
import com.soflyit.chattask.im.message.service.IMsgReplyService;
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
 * 消息回复统计Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/msgReply")
@Slf4j
@Api(tags = {"消息回复统计"})
public class MsgReplyController extends BaseController {
    @Autowired
    private IMsgReplyService msgReplyService;


    @RequiresPermissions("message:msgReply:list")
    @GetMapping("/list")
    @ApiOperation("查询消息回复统计列表")
    public TableDataInfo list(MsgReply msgReply) {
        startPage();
        List<MsgReply> list = msgReplyService.selectMsgReplyList(msgReply);
        return getDataTable(list);
    }


    @RequiresPermissions("message:msgReply:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息回复统计详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgReplyService.selectMsgReplyById(id));
    }


    @RequiresPermissions("message:msgReply:add")
    @PostMapping
    @ApiOperation("新增消息回复统计")
    public AjaxResult add(@RequestBody MsgReply msgReply) {
        msgReply.setCreateBy(SecurityUtils.getUserId());
        return toAjax(msgReplyService.insertMsgReply(msgReply));
    }


    @RequiresPermissions("message:msgReply:edit")
    @PutMapping
    @ApiOperation("修改消息回复统计")
    public AjaxResult edit(@RequestBody MsgReply msgReply) {
        msgReply.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgReplyService.updateMsgReply(msgReply));
    }


    @RequiresPermissions("message:msgReply:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除消息回复统计")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(msgReplyService.deleteMsgReplyByIds(ids));
    }
}
