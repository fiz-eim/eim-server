package com.soflyit.system.api;

import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.factory.RemoteDeptFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @author soflyit
 * @Description: 部门服务
 * @date 2022/10/31 16:42
 */
@FeignClient(contextId = "remoteDeptService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory =
        RemoteDeptFallbackFactory.class)
public interface RemoteDeptService {


    @PostMapping("/dept/info/byDeptIds")
    R<List<SysDept>> getDeptInfoByDeptIds(@RequestBody List<Long> deptIds,
                                          @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping("/dept/info/byDeptCodes")
    R<List<SysDept>> getDeptInfoByDeptCodes(@RequestBody List<String> deptCodes,
                                            @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @GetMapping("/dept/info/allDepts")
    R<List<SysDept>> getAllDept(@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
