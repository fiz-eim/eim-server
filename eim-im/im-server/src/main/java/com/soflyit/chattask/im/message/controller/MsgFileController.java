package com.soflyit.chattask.im.message.controller;

import com.soflyit.chattask.im.message.domain.entity.MsgFile;
import com.soflyit.chattask.im.message.service.IMsgFileService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 消息文件Controller
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@RestController
@RequestMapping("/msgFile")
@Slf4j
@Api(tags = {"消息文件"})
public class MsgFileController extends BaseController {
    @Autowired
    private IMsgFileService msgFileService;



    @RequiresLogin
    @GetMapping("/list")
    @ApiOperation("查询消息文件列表")
    public TableDataInfo list(MsgFile msgFile) {
        startPage();
        List<MsgFile> list = msgFileService.selectMsgFileList(msgFile);
        return getDataTable(list);
    }


    @GetMapping("/list/file")
    @ApiOperation("查询消息文件列表")
    public AjaxResult<List<MsgFile>> listFile(MsgFile msgFile) {
        return msgFileService.selectNormalFile(msgFile);
    }


    @GetMapping("/list/media")
    @ApiOperation("查询消息文件列表")
    public AjaxResult<List<MsgFile>> listMediaFile(MsgFile msgFile) {
        return msgFileService.selectMediaFile(msgFile);

    }



    @RequiresLogin
    @GetMapping(value = "/{id}")
    @ApiOperation("获取消息文件详细信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(msgFileService.selectMsgFileById(id));
    }




    @RequiresLogin
    @GetMapping(value = "/{id}/info")
    @ApiOperation("获取文件元数据信息")
    @ApiImplicitParam(name = "id", value = "主键", dataType = "Long", paramType = "path")
    public AjaxResult getFileInfo(@PathVariable("id") Long id) {
        return msgFileService.getFileInfo(id);
    }


    @PostMapping
    @ApiOperation("新增消息文件")
    public AjaxResult add(@RequestParam(name = "file") MultipartFile file, @RequestParam(name = "channelId") Long channelId,
                          @RequestParam(name = "uuid", required = false) String uuid,
                          @RequestParam(name = "messageId", required = false) Long messageId) {
        log.debug("=========> 新增消息文件====》");
        if (file == null) {
            return AjaxResult.error("文件不能为空");
        }
        if (channelId == null) {
            return AjaxResult.error("频道不能为空");
        }
        return msgFileService.uploadFile(file, channelId, messageId, uuid);
    }


    @RequiresLogin
    @GetMapping("/checkClientFile/{uuid}")
    @ApiOperation("检查文件")
    public AjaxResult checkClientFile(@PathVariable(name = "uuid") String uuid) {
        if (uuid == null) {
            return AjaxResult.error("客户端文件Id不能为空");
        }
        return msgFileService.checkClientFile(uuid);
    }


    @RequiresLogin
    @PutMapping
    @ApiOperation("修改消息文件")
    public AjaxResult edit(@RequestBody MsgFile msgFile) {
        msgFile.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(msgFileService.updateMsgFile(msgFile));
    }


    @DeleteMapping("/{id}")
    @ApiOperation("删除消息文件")
    @ApiImplicitParam(name = "id", value = "文件Id", dataType = "Long", paramType = "path", allowMultiple = true)
    public AjaxResult remove(@PathVariable Long id) {
        return msgFileService.deleteMsgFile(id);
    }


    @GetMapping("/{id}/preview")
    @ApiOperation("预览图片")
    @ApiImplicitParam(name = "id", value = "文件Id", dataType = "Long", paramType = "path", allowMultiple = true)
    public void previewFile(HttpServletResponse response, @PathVariable Long id) {
        msgFileService.previewFile(response, id);

    }

    @GetMapping("/{id}/thumbnail")
    @ApiOperation("缩略图")
    @ApiImplicitParam(name = "id", value = "文件Id", dataType = "Long", paramType = "path", allowMultiple = true)
    public void thumbnailFile(HttpServletResponse response, @PathVariable Long id) {
        msgFileService.thumbnailFile(response, id);

    }

    @GetMapping("/{id}/download")
    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "id", value = "文件Id", dataType = "Long", paramType = "path", allowMultiple = true)
    public void downloadFile(HttpServletResponse response, @PathVariable Long id) {
        msgFileService.downloadFile(response, id);
    }


}
