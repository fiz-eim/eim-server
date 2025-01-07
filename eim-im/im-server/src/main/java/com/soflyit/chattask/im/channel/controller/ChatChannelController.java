package com.soflyit.chattask.im.channel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.domain.entity.ChatChannel;
import com.soflyit.chattask.im.channel.domain.param.ChannelAddParam;
import com.soflyit.chattask.im.channel.domain.param.ChannelQueryParam;
import com.soflyit.chattask.im.channel.domain.param.MemberRemoveParam;
import com.soflyit.chattask.im.channel.domain.param.UserChannelQueryParam;
import com.soflyit.chattask.im.channel.domain.vo.ChannelVo;
import com.soflyit.chattask.im.channel.domain.vo.MemberVo;
import com.soflyit.chattask.im.channel.service.IChannelMemberService;
import com.soflyit.chattask.im.channel.service.IChatChannelService;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 频道Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/chatChannel")
@Slf4j
@Api(tags = {"频道"})
public class ChatChannelController extends BaseController {

    private IChatChannelService chatChannelService;

    private IChannelMemberService channelMemberService;


    @GetMapping("/list")
    @ApiOperation("查询频道列表")
    public TableDataInfo<ChatChannel> list(ChannelQueryParam condition) {
        startPage();
        List<ChatChannel> list = chatChannelService.selectChatChannelList(condition);
        return getDataTable(list);
    }


    @GetMapping("/listUserChannel")
    @ApiOperation("查询频道列表")
    public TableDataInfo<ChannelVo> listUserChannel(UserChannelQueryParam condition) {
        List<ChannelVo> list = chatChannelService.selectUserChannelList(condition);
        return getDataTable(list);
    }


    @GetMapping("/user/{userId}/members")
    @ApiOperation("查询频道列表")
    public TableDataInfo<ChannelMember> listUserMember(@PathVariable("userId") Long userId) {
        List<ChannelMember> list = chatChannelService.selectUserMemberList(userId, Boolean.FALSE);
        return getDataTable(list);
    }


    @PostMapping
    @ApiOperation("新增频道")
    public AjaxResult add(@RequestBody ChannelAddParam chatChannel) {
        ChatChannel channel = chatChannelService.insertChatChannel(chatChannel);
        return AjaxResult.success(channel);
    }


    @PostMapping("/{channelId}/createFolder")
    @ApiOperation("创建频道目录")
    public AjaxResult createFolder(@PathVariable("channelId") Long channelId) {
        chatChannelService.createFolder(channelId);
        return AjaxResult.success();
    }


    @PutMapping
    @ApiOperation("修改频道")
    public AjaxResult edit(@RequestBody ChatChannel chatChannel) {
        if (chatChannel == null) {
            return AjaxResult.error("修改频道失败，频道不存在");
        }

        if (chatChannel.getId() == null) {
            String paramName = "id";
            String msg = "修改频道失败，频道Id不能为空";
            return AjaxResult.error(msg);
        }

        if (StringUtils.isEmpty(chatChannel.getName())) {
            String msg = "修改频道失败，频道名称不能为空";
            return AjaxResult.error(msg);
        }

        if (chatChannel.getName().length() > 200) {
            String msg = "修改频道失败，频道名称不能超过200字符";
            return AjaxResult.error(msg);
        }

        return chatChannelService.updateChatChannel(chatChannel);
    }


    @GetMapping(value = "/{id}")
    @ApiOperation("获取频道详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfoById(@PathVariable("id") Long id) {
        return chatChannelService.getChannelInfo(id);
    }


    @DeleteMapping("/{id}")
    @ApiOperation("删除频道")
    @ApiImplicitParam(name = "id", value = "频道Id", dataType = "Long", paramType = "path")
    public AjaxResult deleteById(@PathVariable("id") Long id) {

        if (id == null) {
            String msg = "删除频道失败，频道Id不能为空";
            return AjaxResult.error(msg);
        }
        return chatChannelService.deleteChatChannel(id);
    }


    @PostMapping("{id}/restore")
    @ApiOperation("恢复频道")
    @ApiImplicitParam(name = "id", value = "频道Id", dataType = "Long", paramType = "path")
    public AjaxResult restore(@PathVariable("id") Long id) {
        if (id == null) {
            String msg = "恢复频道失败，频道Id不能为空";
            return AjaxResult.error(msg);
        }
        return chatChannelService.restoreChatChannel(id);
    }


    @GetMapping("/deleted")
    @ApiOperation("查询已归档频道")
    public TableDataInfo<ChatChannel> getDeletedChannels(ChannelQueryParam condition) {
        startPage();
        List<ChatChannel> list = chatChannelService.selectChatChannelListByDelete();
        return getDataTable(list);
    }


    @GetMapping(value = "/{id}/memberCount")
    @ApiOperation("查询频道成员数量")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "String", paramType = "path")
    public AjaxResult memberCount(@PathVariable("id") Long id) {
        return channelMemberService.getMemberCount(id);
    }


    @GetMapping("/{id}/members")
    @ApiOperation("查询频道成员列表")
    public TableDataInfo getMembers(@PathVariable("id") Long channelId) {
        TableDataInfo result;
        if (channelId == null) {
            result = new TableDataInfo<>();
            result.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            result.setTotal(0);
            result.setRows(new ArrayList());
            String msg = "获取频道成员失败，频道Id不能为空";
            result.setMsg(msg);
        } else {
            ChannelMember condition = new ChannelMember();
            condition.setChannelId(channelId);
            List<MemberVo> list = channelMemberService.selectChannelMemberList(condition);
            result = getDataTable(list);
        }
        return result;
    }


    @GetMapping(value = "/{id}/members/{userId}")
    @ApiOperation("查询频道成员详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getMemberInfo(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {

        if (id == null) {
            String msg = "获取成员信息失败，频道Id不能为空";
            return AjaxResult.error(msg);
        }

        ChannelMember condition = new ChannelMember();
        condition.setChannelId(id);
        condition.setUserId(userId);

        return channelMemberService.getMemberInfo(condition);
    }


    @PostMapping("/{id}/members")
    @ApiOperation("新增频道成员")
    public AjaxResult addChannelMember(@PathVariable("id") Long id, @RequestBody List<ChannelMember> channelMember) throws JsonProcessingException {
        return channelMemberService.addChannelMember(channelMember, id, Boolean.FALSE);
    }


    @DeleteMapping("/{id}/members")
    @ApiOperation("删除成员")
    public AjaxResult removeMember(@PathVariable("id") Long channelId, @RequestBody MemberRemoveParam param) {


        Long currentUserId = SecurityUtils.getUserId();

        if (currentUserId == null) {
            String msg = "操作失败，用户没有登录";
            return AjaxResult.error(msg);
        }
        if (CollectionUtils.isEmpty(param.getMemberList())) {
            String msg = "操作失败，成员列表不能为空";
            return AjaxResult.error(msg);
        }


        param.setChannelId(channelId);
        return channelMemberService.removeMember(param);
    }


    @DeleteMapping("/{id}/members/leave")
    @ApiOperation("离开频道")
    public AjaxResult leaveChannel(@PathVariable("id") Long channelId) {

        Long currentUserId = SecurityUtils.getUserId();

        if (currentUserId == null) {
            String msg = "操作失败，用户没有登录";
            return AjaxResult.error(msg);
        }
        return channelMemberService.leaveChannel(channelId);
    }

    @Autowired
    public void setChatChannelService(IChatChannelService chatChannelService) {
        this.chatChannelService = chatChannelService;
    }

    @Autowired
    public void setChannelMemberService(IChannelMemberService channelMemberService) {
        this.channelMemberService = channelMemberService;
    }

}
