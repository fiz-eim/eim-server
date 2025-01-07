package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteUserService;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户服务降级处理
 *
 * @author soflyit
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {

            @Override
            public R<LoginUser> getUserInfo(Long appId, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<LoginUser> getUserInfo(String username, Long appId, Long deptId, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<LoginUser> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<LoginUser>> getUserInfoByPhone(String phone, Long appId, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            @Override
            public R<SysUser> getInfoById(Long userId, String source) {
                return R.fail("用户查询失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getNickNameByIds(List<Long> userIds, String source) {
                return R.fail("用户查询失败:" + throwable.getMessage());
            }

            @Override
            public R<SysUser> getUserInfoByExt1(Long userId, String source) {
                return R.fail("用户查询失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getUserListByIds(Long[] ids, String source) {
                return R.fail("用户查询失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> listByRemote(String userName, String phoneNumber, int pageNum, int pageSize) {
                return R.fail("用户查询失败:" + throwable.getMessage());
            }

            @Override
            public R<List<SysUser>> getAllSimpleUserList() {
                return R.fail("查询全部用户信息失败:" + throwable.getMessage());
            }

            @Override
            public R<String> getUserAvatarByUserId(String userId, String source) {
                return R.fail("根据用户Id查询用户头像失败:" + throwable.getMessage());
            }

            @Override
            public R<Map<Long, String>> getUserAvatarByUserIds(List<Long> userIds, String source) {
                return R.fail("根据用户Id列表查询用户头像失败:" + throwable.getMessage());
            }
        };
    }
}
