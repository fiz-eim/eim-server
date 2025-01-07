package com.soflyit.chattask.dx.common.remoteApi;

import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteUserService;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.soflyit.chattask.dx.common.remoteApi
 *
 * @Description:
 * @date: 2023/11/28 15:19
 * @author: dengchang/dddgoal@163.com
 */
@Component
public class RemoteUserUtil {

    public final Map<Long,SysUser> userMap = new HashMap<>();

    @Resource
    private RemoteUserService remoteUserService;


    public String getUserNickNameByUserId(Long userId){
        if (userId == null){
            return null;
        }
        if (userMap.containsKey(userId)){
            return userMap.get(userId).getNickName();
        }
        SysUser byUserId = getByUserId(userId);
        return byUserId == null ? null : byUserId.getNickName();
    }


    public SysUser getByUserId(Long userId){
        R<LoginUser> infoById = remoteUserService.getUserInfo(userId, SecurityConstants.INNER);
        if (R.SUCCESS == infoById.getCode()){
            if (infoById.getData() != null){
                userMap.put(userId,infoById.getData().getSysUser());
                return infoById.getData().getSysUser();
            }
        }
        return null;
    }

}
