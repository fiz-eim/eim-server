package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteAvatarApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 用户服务降级处理
 *
 * @author soflyit
 */
@Component
public class RemoteAvatarFallbackFactory implements FallbackFactory<RemoteAvatarApi> {
    private static final Logger log = LoggerFactory.getLogger(RemoteAvatarFallbackFactory.class);

    @Override
    public RemoteAvatarApi create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteAvatarApi() {

            @Override
            public R<String> getUserAvatarByUserId(String userId, String source) {
                return R.fail("根据用户Id查询用户头像失败:" + throwable.getMessage());
            }

            @Override
            public R<Map<String, String>> getAvatarFullPaths(List<String> paths, String source) {
                return R.fail("获取图标失败:" + throwable.getMessage());
            }

            @Override
            public R<Map<Long, String>> getUserAvatarByUserIds(List<Long> userIds, String source) {
                return R.fail("根据用户Id列表查询用户头像失败:" + throwable.getMessage());
            }

            @Override
            public R<String> uploadIcon(MultipartFile file) {
                return R.fail("上传图标失败:" + throwable.getMessage());
            }
        };
    }
}
