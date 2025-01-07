package com.soflyit.chattask.im.netty.service;

import com.soflyit.chattask.lib.netty.server.domain.WebsocketUserId;
import com.soflyit.chattask.lib.netty.server.service.ImBusinessService;
import com.soflyit.common.security.auth.AuthUtil;
import com.soflyit.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * websocket 业务服务<br>
 * 详细说明
 *
 * @author Toney
 * @date 2023-12-05 14:10
 */
@Component
@Slf4j
public class ImBusinessServiceImpl implements ImBusinessService<Long> {
    @Override
    public WebsocketUserId<Long> validateToken(String token) {
        log.warn("验证token：{}", token);

        LoginUser loginUser = AuthUtil.getLoginUser(token);
        if (loginUser == null) {
            return null;
        }
        return new WebsocketUserId<>(loginUser.getUserid());
    }
}
