package com.soflyit.system.api;

import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.domain.SysAuthApp;
import com.soflyit.system.api.factory.RemoteAppFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 应用管理接口
 *
 * @author Toney
 */
@FeignClient(contextId = "remoteAppService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteAppFallbackFactory.class)
public interface RemoteAppService {


    @PostMapping("/app/add")
    R<SysAuthApp> addApp(@RequestBody SysAuthApp appInfo);



    @PostMapping("/app/delete")
    R<SysAuthApp> deleteApp(@RequestParam("appId") Long appId);

}
