package com.soflyit.chattask.im.message.controller;


import com.soflyit.chattask.im.message.domain.entity.Message;
import com.soflyit.chattask.im.message.service.MsgFavoriteService;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.security.annotation.RequiresLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/msg/favorite")
@Slf4j
@Api(tags = {"收藏消息"})
public class MsgFavoriteController {

    @Autowired
    private MsgFavoriteService msgFavoriteService;



    @RequiresLogin
    @GetMapping("/list")
    @ApiOperation("收藏消息列表")
    public AjaxResult listFavorite(Message message) {
        return msgFavoriteService.getFavoriteMessages();
    }



    @RequiresLogin
    @PostMapping("/add/{msgId}")
    @ApiOperation("收藏消息")
    @ApiImplicitParam(name = "msgId", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult addFavorite(@PathVariable Long msgId) {
        return msgFavoriteService.addFavorite(msgId);
    }



    @RequiresLogin
    @PostMapping("/delete/{id}")
    @ApiOperation("删除收藏消息")
    @ApiImplicitParam(name = "id", value = "消息Id", dataType = "Long", paramType = "path")
    public AjaxResult deleteFavorite(@PathVariable Long id) {
        return msgFavoriteService.deleteFavorite(id);
    }


}
