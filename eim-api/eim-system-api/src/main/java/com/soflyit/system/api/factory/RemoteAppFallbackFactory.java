package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteAppService;
import com.soflyit.system.api.domain.SysAuthApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RemoteAppFallbackFactory implements FallbackFactory<RemoteAppService> {

    @Override
    public RemoteAppService create(Throwable cause) {
        return new RemoteAppService() {
            @Override
            public R<SysAuthApp> addApp(SysAuthApp appInfo) {
                return R.fail("添加应用失败：" + cause.getMessage());
            }

            @Override
            public R<SysAuthApp> deleteApp(Long appId) {
                return R.fail("删除应用失败：" + cause.getMessage());
            }

        };
    }
}
