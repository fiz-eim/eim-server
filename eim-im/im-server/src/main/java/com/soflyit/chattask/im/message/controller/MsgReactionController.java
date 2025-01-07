package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgReaction;
import com.soflyit.chattask.im.message.service.IMsgReactionService;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresLogin;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 消息回应Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/msgReaction")
@Slf4j
@Api(tags = {"消息回应"})
public class MsgReactionController extends BaseController {
    @Autowired
    private IMsgReactionService msgReactionService;


    @RequiresLogin
    @GetMapping("/list")
    public TableDataInfo list( MsgReaction msgReaction) {
        startPage();
        List<MsgReaction> list = msgReactionService.selectMsgReactionList(msgReaction);
        return getDataTable(list);
    }


    @RequiresLogin
    @PostMapping("/export")
    @ApiOperation("导出消息回应列表")
    public void export(@ApiIgnore HttpServletResponse response, MsgReaction msgReaction) {
        List<MsgReaction> list = msgReactionService.selectMsgReactionList(msgReaction);
        ExcelUtil<MsgReaction> util = new ExcelUtil<MsgReaction>(MsgReaction.class);
        util.exportExcel(response, list, "消息回应数据");
    }


    @RequiresLogin
    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息回应详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgReactionService.selectMsgReactionById(id));
    }


    @RequiresLogin
    @PostMapping
    @ApiOperation("新增消息回应")
    public AjaxResult add(@RequestBody MsgReaction msgReaction) {
        if (msgReaction == null) {
            return AjaxResult.error("回应消息失败，参数不能为空");
        }
        if (msgReaction.getMsgId() == null) {
            return AjaxResult.error("回应消息失败，消息Id不能为空");
        }
        if (msgReaction.getChannelId() == null) {
            return AjaxResult.error("回应消息失败，频道Id不能为空");
        }

        if (StringUtils.isEmpty(msgReaction.getEmojiName())) {
            return AjaxResult.error("回应消息失败，表情图标不能为空");
        }

        return msgReactionService.addReaction(msgReaction);
    }


    @RequiresLogin
    @PutMapping
    @ApiOperation("修改消息回应")
    public AjaxResult edit(@RequestBody MsgReaction msgReaction) {
        msgReaction.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgReactionService.updateMsgReaction(msgReaction));
    }


    @RequiresLogin
    @DeleteMapping("/{id}")
    @ApiOperation("删除消息回应")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable("id") Long id) {
        if (id == null) {
            return AjaxResult.error("撤销回应失败，回应Id不能为空");
        }
        return msgReactionService.removeReaction(id);
    }
}
