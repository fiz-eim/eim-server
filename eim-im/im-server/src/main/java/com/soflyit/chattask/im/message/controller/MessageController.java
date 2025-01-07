package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgReply;
import com.soflyit.chattask.im.message.domain.param.*;
import com.soflyit.chattask.im.message.domain.vo.MessageProp;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.service.*;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.annotation.RequiresLogin;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 消息Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/message")
@Slf4j
@Api(tags = {"消息"})
public class MessageController extends BaseController {
    private IMessageService messageService;

    private IMsgForwardService msgForwardService;

    private IMsgReplyService msgReplyService;

    @Autowired
    private IMemberDeleteMsgService memberDeleteMsgService;

    @Autowired
    private MsgTextTagService msgTextTagService;


    @GetMapping("/list/channel")
    @ApiOperation("查询频道消息列表")
    public AjaxResult<List<MessageVo>> list(MessageQueryParam message) {
        return messageService.selectChannelMessageList(message);
    }


    @RequiresLogin
    @GetMapping("/list/msg")
    @ApiOperation("查询消息列表")
    public AjaxResult<List<MessageVo>> listMsg(MessageQueryParam message) {
        return messageService.selectMessageList(message);
    }


    @GetMapping("/list/mention")
    @ApiOperation("查询提及消息Id列表")
    public AjaxResult<List<Long>> selectMentionMessageIdList(MessageMentionQueryParam message) {
        return messageService.selectMentionMessageIdList(message);
    }



    @GetMapping("/list/channel/{channelId}/message/unread")
    @ApiOperation("查询未读消息")
    public AjaxResult<List<MessageVo>> listUserMsg(UserMessageQueryParam message, @PathVariable(name = "channelId") Long channelId) {
        message.setChannelId(channelId);
        return messageService.selectUserChannelMessageList(message);
    }


    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(messageService.selectMessageById(id));
    }


    @PostMapping
    @ApiOperation("新增消息")
    public AjaxResult add(@RequestBody MessageAddParam message) {

        if (message == null) {
            return AjaxResult.error("发送消息失败，参数不能为空");
        }

        Long channelId = message.getChannelId();
        if (channelId == null) {
            return AjaxResult.error("发送消息失败，频道Id不能为空");
        }

        Short messageType = message.getType();
        if (messageType == null) {
            return AjaxResult.error("发送消息失败，消息类型不能为空");
        }

        return messageService.createMessage(message);
    }


    @GetMapping("/{id}/replyInfo")
    @ApiOperation("回复消息统计")
    public AjaxResult getReplyInfo(@PathVariable(name = "id") Long messageId) {
        return AjaxResult.error("功能开发中");
    }


    @PutMapping
    @ApiOperation("修改消息")
    public AjaxResult edit(@RequestBody Message message) {
        message.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(messageService.updateMessage(message));
    }


    @RequiresLogin
    @PostMapping("/{msgId}/markTag")
    @ApiOperation("标记消息")
    public AjaxResult tagMsg(@PathVariable("msgId") Long msgId) {
        return messageService.markTag(msgId);
    }


    @RequiresLogin
    @PostMapping("/{msgId}/addTextTag")
    @ApiOperation("添加消息标签")
    public AjaxResult addTextTag(@PathVariable("msgId") Long msgId, @RequestBody MsgTextTagAddParam textTag) {
        textTag.setMsgId(msgId);
        return msgTextTagService.addTag(textTag);
    }


    @RequiresLogin
    @PostMapping("/{msgId}/deleteTextTag")
    @ApiOperation("删除消息标签")
    public AjaxResult deleteTextTag(@PathVariable("msgId") Long msgId, @RequestBody MsgTextTagAddParam textTag) {
        textTag.setMsgId(msgId);
        return msgTextTagService.deleteTag(textTag);
    }



    @RequiresLogin
    @PostMapping("/{msgId}/unmarkTag")
    @ApiOperation("取消标记消息")
    public AjaxResult unmarkTag(@PathVariable("msgId") Long msgId) {
        return messageService.unmarkTag(msgId);
    }



    @DeleteMapping("/{id}")
    @ApiOperation("删除消息（撤回消息）")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long id) {
        return messageService.deleteMessageById(id);
    }


    @PostMapping("/deleteMemberMsg/{id}")
    @ApiOperation("频道成员删除消息")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult deleteMemberMsg(@PathVariable Long id, @RequestBody List<Long> msgIds) {
        return memberDeleteMsgService.deleteMessageById(id, msgIds);
    }


    @RequiresLogin
    @GetMapping("/pinned/list")
    @ApiOperation("固定消息")
    public AjaxResult listPinned(Message message) {
        return messageService.getPinnedMessages(message);
    }


    @PostMapping("/{msgId}/pin")
    @ApiOperation("固定消息")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult pinnedMessage(@PathVariable Long msgId) {
        return messageService.pinnedMessage(msgId);
    }


    @PostMapping("/{msgId}/unpin")
    @ApiOperation("取消固定消息")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult unpinnedMessage(@PathVariable Long msgId) {
        return messageService.unpinnedMessage(msgId);
    }


    @PostMapping("/{msgId}/ack")
    @ApiOperation("确认消息")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult ackMessage(@PathVariable Long msgId) {
        return messageService.ackMessage(msgId);
    }


    @PostMapping("/{msgId}/cancelAck")
    @ApiOperation("取消确认")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult cancelAckMessage(@PathVariable Long msgId) {
        return messageService.cancelAckMessage(msgId);
    }


    @RequiresLogin
    @PostMapping("/{msgId}/markTop")
    @ApiOperation("置顶消息")
    @ApiImplicitParam(name = "msgId", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult markTop(@PathVariable Long msgId) {
        return messageService.markTop(msgId);
    }


    @RequiresLogin
    @PostMapping("/{msgTopId}/markUnTop")
    @ApiOperation("取消置顶消息")
    @ApiImplicitParam(name = "msgTopId", value = "消息置顶Id", dataType = "Long", paramType = "path")
    public AjaxResult markUnTop(@PathVariable Long msgTopId) {
        return messageService.cancelTop(msgTopId);
    }


    @RequiresLogin
    @PostMapping("/{msgId}/closeTop")
    @ApiOperation("关闭置顶消息")
    @ApiImplicitParam(name = "msgId", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult closeTop(@PathVariable Long msgId) {
        return messageService.closeTop(msgId);
    }


    @RequiresLogin
    @GetMapping("/topicList")
    @ApiOperation("查询话题列表")
    public AjaxResult getThread(MsgReply reply) {

        return msgReplyService.selectTopicList(reply);
    }


    @RequiresLogin
    @GetMapping("/topicList/mentionMe")
    @ApiOperation("查询提及我的话题列表")
    public AjaxResult getMentionMeTopicList(MsgReply reply) {
        return msgReplyService.selectMentionMeTopicList(reply);
    }



    @RequiresLogin
    @GetMapping("/mentionMe")
    @ApiOperation("查询提及我的消息")
    public AjaxResult getMentionMeList(Message condition) {
        return msgReplyService.selectMentionMeList(condition);
    }



    @RequiresLogin
    @GetMapping("/{msgId}/thread")
    @ApiOperation("查询回复消息（消息串）")
    @ApiImplicitParam(name = "msgId", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult getThread(@PathVariable Long msgId, ThreadQueryParam threadQueryParam) {
        if (msgId == null) {
            String msg = "获取话题失败，消息Id不能为空";
            return AjaxResult.error(msg);
        }

        return messageService.selectThreadMessages(msgId, threadQueryParam);
    }



    @PostMapping("/forward")
    @ApiOperation("消息转发")
    public AjaxResult forward(@RequestBody MessageForwardParam forwardParam) {

        if (forwardParam == null) {
            String msg = "消息转发失败，参数不能为空";
            return AjaxResult.error(msg);
        }
        if (forwardParam.getForwardType() == null) {
            String msg = "消息转发失败，参数【forwardType】不能为空";
            return AjaxResult.error(msg);
        }
        if (CollectionUtils.isEmpty(forwardParam.getForwardMsgIds())) {
            String msg = "消息转发失败，参数【forwardMsgId】不能为空";
            return AjaxResult.error(msg);
        }
        if (CollectionUtils.isEmpty(forwardParam.getChannelIds())) {
            String msg = "消息转发失败，参数【channelIds】不能为空";
            return AjaxResult.error(msg);
        }
        return msgForwardService.forwardMessage(forwardParam);
    }


    @RequiresLogin
    @PostMapping("/replay/{msgId}")
    @ApiOperation("回复消息")
    public AjaxResult replayMsg(@PathVariable("msgId") Long msgId, @RequestBody MessageAddParam message) {

        if (message == null) {
            return AjaxResult.error("发送消息失败：消息数据为空");
        }

        Long channelId = message.getChannelId();
        if (channelId == null) {
            return AjaxResult.error("回复消息失败，频道Id不能为空");
        }

        Short messageType = message.getType();
        if (messageType == null) {
            return AjaxResult.error("回复消息失败，消息类型不能为空");
        }
        MessageProp prop = message.getProps();
        if (prop == null) {
            prop = new MessageProp();
            prop.setReplyFlag(Boolean.TRUE);
            prop.setReplyMsgId(msgId);
        }

        return messageService.createMessage(message);
    }



    @RequiresLogin
    @GetMapping("/recall/config")
    @ApiOperation("查询撤回消息配置")
    public AjaxResult getRecallConfig() {
        return AjaxResult.success(new HashMap<>());
    }

    @Autowired
    public void setMessageService(IMessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setMsgForwardService(IMsgForwardService msgForwardService) {
        this.msgForwardService = msgForwardService;
    }

    @Autowired
    public void setMsgReplyService(IMsgReplyService msgReplyService) {
        this.msgReplyService = msgReplyService;
    }
}
