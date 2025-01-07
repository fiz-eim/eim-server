package com.soflyit.system.api;

import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.factory.RemoteUserFallbackFactory;
import com.soflyit.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author soflyit
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {


    @GetMapping("/user/getUserInfo")
    R<LoginUser> getUserInfo(@RequestParam(name = "appId", required = false) Long appId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/user/info/{username}")
    R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestParam(name = "appId", required = false) Long appId, @RequestParam(name = "deptId", required = false) Long deptId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/user/info-default/{username}")
    R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/user/info-phone/{phone}")
    R<List<LoginUser>> getUserInfoByPhone(@PathVariable("phone") String phone, @RequestParam(name = "appId", required = false) Long appId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping("/user/register")
    R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据用户id查询信息
     *
     * @return
     * @Date 2022/5/31 10:08
     * @Param
     **/
    @GetMapping({"/user/getNickName/{userId}"})
    R<SysUser> getInfoById(@PathVariable(value = "userId", required = false) Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据用户id查询信息
     *
     * @return
     * @Date 2022/5/31 10:08
     * @Param
     **/
    @GetMapping({"/user/getNickNames"})
    R<List<SysUser>> getNickNameByIds(@RequestParam(value = "userIds", required = false) List<Long> userIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping({"/user/getUserInfoByExt1/{userId}"})
    R<SysUser> getUserInfoByExt1(@PathVariable(value = "userId", required = false) Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping({"/user/getUserListByIds/{ids}"})
    R<List<SysUser>> getUserListByIds(@PathVariable(value = "ids", required = false) Long[] ids, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);



    @GetMapping("/user/listByRemote")
    R<List<SysUser>> listByRemote(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "pageNum", required = false) int pageNum,
            @RequestParam(value = "pageSize", required = false) int pageSize
    );


    @GetMapping("/user/getAllSimpleUserList")
    R<List<SysUser>> getAllSimpleUserList();


    @GetMapping("/avatar/{userId}")
    R<String> getUserAvatarByUserId(@PathVariable("userId") String userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/avatar/users")
    R<Map<Long, String>> getUserAvatarByUserIds(@RequestParam("userIds") List<Long> userIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
