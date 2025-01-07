package com.soflyit.system.api;

import com.soflyit.common.core.constant.SecurityConstants;
import com.soflyit.common.core.constant.ServiceNameConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.system.api.domain.SysDictData;
import com.soflyit.system.api.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 远程字典数据服务
 *
 * @author Toney
 */
@FeignClient(contextId = "remoteDictService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteDictDataService {

    @PostMapping("/dict/data/list")
    TableDataInfo<SysDictData> postList(@RequestBody SysDictData dictData);

    @PostMapping(value = "/dict/data/type/{dictType}")
    AjaxResult postDictType(@PathVariable(value = "dictType") String dictType, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/dict/data/getDictLabel/{dictType},{dictValue}")
    R<String> getDictLabel(@PathVariable(value = "dictType", required = false) String dictType,
                           @PathVariable(value = "dictValue", required = false) String dictValue,
                           @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
