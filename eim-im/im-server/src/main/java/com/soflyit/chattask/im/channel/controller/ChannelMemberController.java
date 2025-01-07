package com.soflyit.chattask.im.channel.controller;

import com.soflyit.chattask.im.channel.domain.entity.ChannelMember;
import com.soflyit.chattask.im.channel.service.IChannelMemberService;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 频道成员Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@RestController
@RequestMapping("/channelMember")
@Slf4j
@Api(tags = {"频道成员"})
public class ChannelMemberController extends BaseController {
    @Autowired
    private IChannelMemberService channelMemberService;


    @RequiresPermissions("channel:channelMember:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取频道成员详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(channelMemberService.selectChannelMemberById(id));
    }


    @RequiresPermissions("channel:channelMember:add")
    @PostMapping
    @ApiOperation("新增频道成员")
    public AjaxResult add(@RequestBody ChannelMember channelMember) {
        channelMember.setCreateBy(SecurityUtils.getUserId());
        return toAjax(channelMemberService.insertChannelMember(channelMember));
    }


    @RequiresPermissions("channel:channelMember:edit")
    @PutMapping
    @ApiOperation("修改频道成员")
    public AjaxResult edit(@RequestBody ChannelMember channelMember) {
        channelMember.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(channelMemberService.updateChannelMember(channelMember));
    }


    @RequiresPermissions("channel:channelMember:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除频道成员")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(channelMemberService.deleteChannelMemberByIds(ids));
    }


    @RequiresPermissions("channel:channelMember:query")
    @GetMapping(value = "/{id}/channel/{channelId}/unread")
    @ApiOperation("获取频道成员详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getUnReadCount(@PathVariable("id") Long userId, @PathVariable("channelId") Long channelId) {
        return AjaxResult.success(channelMemberService.getUnreadCount(userId, channelId));
    }

}

