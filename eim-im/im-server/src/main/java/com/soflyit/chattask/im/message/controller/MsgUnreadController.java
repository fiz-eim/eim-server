package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgUnread;
import com.soflyit.chattask.im.message.service.IMsgUnreadService;
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
 * 消息未读明细Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@RestController
@RequestMapping("/msgUnread")
@Slf4j
@Api(tags = {"消息未读明细"})
public class MsgUnreadController extends BaseController {
    @Autowired
    private IMsgUnreadService msgUnreadService;


    @RequiresLogin
    @GetMapping("/list")
    @ApiOperation("查询消息未读明细列表")
    public TableDataInfo list(MsgUnread msgUnread) {
        startPage();
        List<MsgUnread> list = msgUnreadService.selectMsgUnreadList(msgUnread);
        return getDataTable(list);
    }


    @RequiresLogin
    @PostMapping("/export")
    @ApiOperation("导出消息未读明细列表")
    public void export(@ApiIgnore HttpServletResponse response, MsgUnread msgUnread) {
        List<MsgUnread> list = msgUnreadService.selectMsgUnreadList(msgUnread);
        ExcelUtil<MsgUnread> util = new ExcelUtil<MsgUnread>(MsgUnread.class);
        util.exportExcel(response, list, "消息未读明细数据");
    }


    @RequiresLogin
    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息未读明细详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgUnreadService.selectMsgUnreadById(id));
    }


    @RequiresLogin
    @PostMapping
    @ApiOperation("新增消息未读明细")
    public AjaxResult add(@RequestBody MsgUnread msgUnread) {
        msgUnread.setCreateBy(SecurityUtils.getUserId());
        return toAjax(msgUnreadService.insertMsgUnread(msgUnread));
    }


    @RequiresLogin
    @PutMapping
    @ApiOperation("修改消息未读明细")
    public AjaxResult edit(@RequestBody MsgUnread msgUnread) {
        msgUnread.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgUnreadService.updateMsgUnread(msgUnread));
    }


    @RequiresLogin
    @DeleteMapping("/{ids}")
    @ApiOperation("删除消息未读明细")
    @ApiImplicitParam(name = "ids", value = "根据IDS删除对应信息", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(msgUnreadService.deleteMsgUnreadByIds(ids));
    }
}
