package com.soflyit.chattask.im.channel.controller;

import com.soflyit.chattask.im.channel.domain.entity.HisChannelMember;
import com.soflyit.chattask.im.channel.service.IHisChannelMemberService;
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
 * 频道成员历史Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/hisChannelMember")
@Slf4j
@Api(tags = {"频道成员历史"})
public class HisChannelMemberController extends BaseController {
    @Autowired
    private IHisChannelMemberService hisChannelMemberService;


    @RequiresPermissions("channel:hisChannelMember:list")
    @GetMapping("/list")
    @ApiOperation("查询频道成员历史列表")
    public TableDataInfo list(HisChannelMember hisChannelMember) {
        startPage();
        List<HisChannelMember> list = hisChannelMemberService.selectHisChannelMemberList(hisChannelMember);
        return getDataTable(list);
    }


    @RequiresPermissions("channel:hisChannelMember:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取频道成员历史详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(hisChannelMemberService.selectHisChannelMemberById(id));
    }


    @RequiresPermissions("channel:hisChannelMember:add")
    @PostMapping
    @ApiOperation("新增频道成员历史")
    public AjaxResult add(@RequestBody HisChannelMember hisChannelMember) {
        hisChannelMember.setCreateBy(SecurityUtils.getUserId());
        return toAjax(hisChannelMemberService.insertHisChannelMember(hisChannelMember));
    }


    @RequiresPermissions("channel:hisChannelMember:edit")
    @PutMapping
    @ApiOperation("修改频道成员历史")
    public AjaxResult edit(@RequestBody HisChannelMember hisChannelMember) {
        hisChannelMember.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(hisChannelMemberService.updateHisChannelMember(hisChannelMember));
    }


    @RequiresPermissions("channel:hisChannelMember:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除频道成员历史")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hisChannelMemberService.deleteHisChannelMemberByIds(ids));
    }
}
