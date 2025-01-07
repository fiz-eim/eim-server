package com.soflyit.chattask.im.event.domain;

import com.soflyit.chattask.lib.netty.event.ChatBroadcast;
import com.soflyit.chattask.lib.netty.event.ChatEvent;
import com.soflyit.common.core.utils.ServletUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 基础事件<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-23 14:49
 */
public class BaseImEvent<D extends Serializable, B extends ChatBroadcast> extends ChatEvent<D, B> {

    public BaseImEvent() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null && SecurityUtils.getToken() != null) {
            this.setToken(SecurityUtils.getToken());
            Long userId = SecurityUtils.getUserId();
            if (userId.equals(0L)) {
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null) {
                    this.setUserId(loginUser.getUserid());
                }
            } else {
                this.setUserId(userId);
            }
        }
    }
}
