package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.RefReplyMember;
import com.soflyit.chattask.im.message.service.IRefReplyMemberService;
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
 * 回复消息与用户关系Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/refReplyMember")
@Slf4j
@Api(tags = {"回复消息与用户关系"})
public class RefReplyMemberController extends BaseController {
    @Autowired
    private IRefReplyMemberService refReplyMemberService;


    @RequiresPermissions("message:refReplyMember:list")
    @GetMapping("/list")
    @ApiOperation("查询回复消息与用户关系列表")
    public TableDataInfo list(RefReplyMember refReplyMember) {
        startPage();
        List<RefReplyMember> list = refReplyMemberService.selectRefReplyMemberList(refReplyMember);
        return getDataTable(list);
    }


    @RequiresPermissions("message:refReplyMember:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取回复消息与用户关系详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(refReplyMemberService.selectRefReplyMemberById(id));
    }


    @RequiresPermissions("message:refReplyMember:add")
    @PostMapping
    @ApiOperation("新增回复消息与用户关系")
    public AjaxResult add(@RequestBody RefReplyMember refReplyMember) {
        refReplyMember.setCreateBy(SecurityUtils.getUserId());
        return toAjax(refReplyMemberService.insertRefReplyMember(refReplyMember));
    }


    @RequiresPermissions("message:refReplyMember:edit")
    @PutMapping
    @ApiOperation("修改回复消息与用户关系")
    public AjaxResult edit(@RequestBody RefReplyMember refReplyMember) {
        refReplyMember.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(refReplyMemberService.updateRefReplyMember(refReplyMember));
    }


    @RequiresPermissions("message:refReplyMember:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除回复消息与用户关系")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(refReplyMemberService.deleteRefReplyMemberByIds(ids));
    }
}
