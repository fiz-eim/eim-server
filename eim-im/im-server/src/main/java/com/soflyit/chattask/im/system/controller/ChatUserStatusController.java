package com.soflyit.chattask.im.system.controller;

import com.soflyit.chattask.im.system.domain.ChatUserStatus;
import com.soflyit.chattask.im.system.service.IChatUserStatusService;
import com.soflyit.common.core.exception.base.BaseException;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户在线状态Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/chatUserStatus")
@Slf4j
@Api(tags = {"用户在线状态"})
public class ChatUserStatusController extends BaseController {
    @Autowired
    private IChatUserStatusService chatUserStatusService;


    @RequiresPermissions("system:chatUserStatus:list")
    @GetMapping("/list")
    @ApiOperation("查询用户在线状态列表")
    public TableDataInfo list(ChatUserStatus chatUserStatus) {
        throw new BaseException("功能开发中");
    }


    @RequiresPermissions("system:chatUserStatus:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取用户在线状态详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.error("功能开发中");
    }


    @RequiresPermissions("system:chatUserStatus:add")
    @PostMapping
    @ApiOperation("新增用户在线状态")
    public AjaxResult add(@RequestBody ChatUserStatus chatUserStatus) {
        return AjaxResult.error("功能开发中");
    }


    @RequiresPermissions("system:chatUserStatus:edit")
    @PutMapping
    @ApiOperation("修改用户在线状态")
    public AjaxResult edit(@RequestBody ChatUserStatus chatUserStatus) {
        return AjaxResult.error("功能开发中");
    }


    @RequiresPermissions("system:chatUserStatus:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除用户在线状态")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return AjaxResult.error("功能开发中");
    }
}
