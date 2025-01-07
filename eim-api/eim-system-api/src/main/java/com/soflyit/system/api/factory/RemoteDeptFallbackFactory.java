package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.system.api.RemoteDeptService;
import com.soflyit.system.api.domain.SysDept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author soflyit
 * @Description: 部门服务fallback
 * @date 2022/10/31 16:49
 */
@Slf4j
@Component
public class RemoteDeptFallbackFactory implements FallbackFactory<RemoteDeptService> {


    @Override
    public RemoteDeptService create(Throwable cause) {

        log.error("调用部门服务失败,", cause);
        return new RemoteDeptService() {

            @Override
            public R<List<SysDept>> getDeptInfoByDeptIds(List<Long> deptIds, String source) {
                return R.fail(cause.getMessage());
            }

            @Override
            public R<List<SysDept>> getDeptInfoByDeptCodes(List<String> deptCodes, String source) {
                return R.fail(cause.getMessage());
            }

            @Override
            public R<List<SysDept>> getAllDept(String source) {
                return R.fail(cause.getMessage());
            }
        };
    }
}
