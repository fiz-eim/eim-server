package com.soflyit.chattask.im.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.domain.entity.MsgTextTag;
import com.soflyit.chattask.im.message.domain.param.MsgTextTagAddParam;
import com.soflyit.chattask.im.message.domain.vo.MessageVo;
import com.soflyit.chattask.im.message.domain.vo.TextTagData;
import com.soflyit.chattask.im.message.mapper.MessageMapper;
import com.soflyit.chattask.im.message.mapper.MsgTextTagMapper;
import com.soflyit.chattask.im.message.service.MsgTextTagService;
import com.soflyit.chattask.im.system.service.impl.UserNickNameService;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 文本标签服务
 */
@Service
public class MsgTextTagServiceImpl extends ServiceImpl<MsgTextTagMapper, MsgTextTag> implements MsgTextTagService {

    private MsgTextTagMapper msgTextTagMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessagePushService messagePushService;

    @Autowired
    private UserNickNameService nickNameService;

    @Autowired
    private MessageQueryService messageQueryService;

    @Override
    public AjaxResult addTag(MsgTextTagAddParam textTag) {
        if (textTag == null) {
            return AjaxResult.error("添加文本标签失败, 参数不能为空");
        }
        if (textTag.getMsgId() == null) {
            return AjaxResult.error("添加文本标签失败, 消息Id不能为空");
        }
        if (StringUtils.isEmpty(textTag.getTag())) {
            return AjaxResult.error("添加文本标签失败, 标签不能为空");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (loginUser == null) {
            return AjaxResult.error("添加文本标签失败，无法获取当前用户信息");
        }

        Message message = messageMapper.selectMessageById(textTag.getMsgId());
        if (message == null) {
            return AjaxResult.error("添加文本标签失败：消息不存在");
        }


        MsgTextTag tag = msgTextTagMapper.selectById(textTag.getMsgId());
        if (tag == null) {
            tag = new MsgTextTag();
        }
        TextTagData textTagData = JSON.parseObject(StringUtils.defaultString(tag.getTagData(), "{}"), TextTagData.class);
        List<TextTagData.TextTag> tags = textTagData.getTags();
        if (tags == null) {
            tags = new ArrayList<>();
            textTagData.setTags(tags);
        }
        Map<String, TextTagData.TextTag> tagDataMap = tags.stream().collect(Collectors.toMap(item -> (item.getTag() + item.getColor()), item -> item, (v1, v2) -> v1));

        TextTagData.TextTag tagItemData = tagDataMap.get(textTag.getTag() + textTag.getColor());

        if (tagItemData == null) {
            tagItemData = new TextTagData.TextTag();
            tagItemData.setColor(textTag.getColor());
            tagItemData.setTag(textTag.getTag());
            tagItemData.setDetails(new ArrayList<>());
            tagItemData.setCount(0);
            tags.add(tagItemData);
        }

        List<TextTagData.TagDetail> details = tagItemData.getDetails();
        if (details == null) {
            details = new ArrayList<>();
            tagItemData.setDetails(details);
        }
        if (details.stream().filter(item -> item.getUserId().equals(loginUser.getUserid())).findFirst().isPresent()) {
            return AjaxResult.success();
        }

        TextTagData.TagDetail detail = new TextTagData.TagDetail();
        detail.setUserId(loginUser.getUserid());
        detail.setTagUser(nickNameService.getNickName(loginUser.getUserid()));
        detail.setTagTime(new Date());
        details.add(detail);
        tagItemData.setCount(details.size());

        String tagData = JSON.toJSONString(textTagData);
        tag.setTagData(tagData);
        if (tag.getMsgId() == null) {
            tag.setMsgId(textTag.getMsgId());
            save(tag);
        } else {
            updateById(tag);
        }

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        List<Long> messageIds = new ArrayList<>();
        messageIds.add(message.getId());
        List<MessageVo> messageVos = messageQueryService.processMetaData(messages, messageIds, null, null, null);
        messagePushService.pushMessageTextTagEvent(messageVos.get(0));

        return AjaxResult.success();
    }

    @Override
    public AjaxResult deleteTag(MsgTextTagAddParam textTag) {
        if (textTag == null) {
            return AjaxResult.error("删除文本标签失败, 参数不能为空");
        }
        if (textTag.getMsgId() == null) {
            return AjaxResult.error("删除文本标签失败, 消息Id不能为空");
        }
        if (StringUtils.isEmpty(textTag.getTag())) {
            return AjaxResult.error("删除文本标签失败, 标签不能为空");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (loginUser == null) {
            return AjaxResult.error("删除文本标签失败，无法获取当前用户信息");
        }

        Message message = messageMapper.selectMessageById(textTag.getMsgId());
        if (message == null) {
            return AjaxResult.error("删除文本标签失败：消息不存在");
        }

        MsgTextTag tag = msgTextTagMapper.selectById(textTag.getMsgId());
        if (tag == null) {
            return AjaxResult.success();
        }
        TextTagData textTagData = JSON.parseObject(StringUtils.defaultString(tag.getTagData(), "[]"), TextTagData.class);
        List<TextTagData.TextTag> tags = textTagData.getTags();
        if (tags == null) {
            tags = new ArrayList<>();
            textTagData.setTags(tags);
        }
        Map<String, TextTagData.TextTag> tagDataMap = tags.stream().collect(Collectors.toMap(item -> (item.getTag() + item.getColor()), item -> item, (v1, v2) -> v1));

        TextTagData.TextTag tagItemData = tagDataMap.get(textTag.getTag() + textTag.getColor());

        if (tagItemData == null) {
            return AjaxResult.success();
        }

        List<TextTagData.TagDetail> details = tagItemData.getDetails();
        if (CollectionUtils.isEmpty(details)) {
            return AjaxResult.success();

        }

        Optional<TextTagData.TagDetail> detailOptional = details.stream().filter(item -> item.getUserId().equals(loginUser.getUserid())).findFirst();
        if (!detailOptional.isPresent()) {
            return AjaxResult.success();
        }
        details.remove(detailOptional.get());
        tagItemData.setCount(details.size());
        if (CollectionUtils.isEmpty(details)) {
            tags.remove(tagItemData);
        }
        String tagData = JSON.toJSONString(textTagData);
        tag.setTagData(tagData);
        if (tag.getMsgId() == null) {
            tag.setMsgId(textTag.getMsgId());
            save(tag);
        } else {
            updateById(tag);
        }

        List<Message> messages = new ArrayList<>();
        messages.add(message);
        List<Long> messageIds = new ArrayList<>();
        messageIds.add(message.getId());
        List<MessageVo> messageVos = messageQueryService.processMetaData(messages, messageIds, null, null, null);
        messagePushService.pushMessageTextTagEvent(messageVos.get(0));

        return AjaxResult.success();
    }

    @PostConstruct
    private void init() {
        this.msgTextTagMapper = getBaseMapper();
    }

}
