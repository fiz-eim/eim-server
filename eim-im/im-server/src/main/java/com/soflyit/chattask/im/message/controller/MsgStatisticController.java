package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgStatistic;
import com.soflyit.chattask.im.message.service.IMsgStatisticService;
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
 * 消息读取统计Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/MsgStatistic")
@Slf4j
@Api(tags = {"消息读取统计"})
public class MsgStatisticController extends BaseController {
    @Autowired
    private IMsgStatisticService MsgStatisticService;


    @RequiresPermissions("message:MsgStatistic:list")
    @GetMapping("/list")
    @ApiOperation("查询消息读取统计列表")
    public TableDataInfo list(MsgStatistic msgStatistic) {
        startPage();
        List<MsgStatistic> list = MsgStatisticService.selectMsgStatisticList(msgStatistic);
        return getDataTable(list);
    }


    @RequiresPermissions("message:MsgStatistic:query")
    @GetMapping(value = "/{msgId}")
    @ApiOperation("获取消息读取统计详细信息")
    @ApiImplicitParam(name = "msgId", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("msgId") Long msgId) {
        return AjaxResult.success(MsgStatisticService.selectMsgStatisticByMsgId(msgId));
    }


    @RequiresPermissions("message:MsgStatistic:add")
    @PostMapping
    @ApiOperation("新增消息读取统计")
    public AjaxResult add(@RequestBody MsgStatistic msgStatistic) {
        msgStatistic.setCreateBy(SecurityUtils.getUserId());
        return toAjax(MsgStatisticService.insertMsgStatistic(msgStatistic));
    }


    @RequiresPermissions("message:MsgStatistic:edit")
    @PutMapping
    @ApiOperation("修改消息读取统计")
    public AjaxResult edit(@RequestBody MsgStatistic msgStatistic) {
        msgStatistic.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(MsgStatisticService.updateMsgStatistic(msgStatistic));
    }


    @RequiresPermissions("message:MsgStatistic:remove")
    @DeleteMapping("/{msgIds}")
    @ApiOperation("删除消息读取统计")
    @ApiImplicitParam(name = "msgIds", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] msgIds) {
        return toAjax(MsgStatisticService.deleteMsgStatisticByMsgIds(msgIds));
    }
}
