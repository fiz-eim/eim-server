package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgAck;
import com.soflyit.chattask.im.message.service.IMsgAckService;
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
 * 消息确认Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/msgAck")
@Slf4j
@Api(tags = {"消息确认"})
public class MsgAckController extends BaseController {
    @Autowired
    private IMsgAckService msgAckService;


    @RequiresPermissions("message:msgAck:list")
    @GetMapping("/list")
    @ApiOperation("查询消息确认列表")
    public TableDataInfo list(MsgAck msgAck) {
        startPage();
        List<MsgAck> list = msgAckService.selectMsgAckList(msgAck);
        return getDataTable(list);
    }


    @RequiresPermissions("message:msgAck:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息确认详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgAckService.selectMsgAckById(id));
    }


    @RequiresPermissions("message:msgAck:add")
    @PostMapping
    @ApiOperation("新增消息确认")
    public AjaxResult add(@RequestBody MsgAck msgAck) {
        msgAck.setCreateBy(SecurityUtils.getUserId());
        return toAjax(msgAckService.insertMsgAck(msgAck));
    }


    @RequiresPermissions("message:msgAck:edit")
    @PutMapping
    @ApiOperation("修改消息确认")
    public AjaxResult edit(@RequestBody MsgAck msgAck) {
        msgAck.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgAckService.updateMsgAck(msgAck));
    }


    @RequiresPermissions("message:msgAck:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除消息确认")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(msgAckService.deleteMsgAckByIds(ids));
    }
}
