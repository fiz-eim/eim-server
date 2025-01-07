package com.soflyit.system.api.factory;

import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.system.api.RemoteDictDataService;
import com.soflyit.system.api.domain.SysDictData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictDataService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteDictDataService create(Throwable cause) {

        log.error("字典数据服务调用失败:{}", cause.getMessage());
        return new RemoteDictDataService() {
            @Override
            public TableDataInfo postList(SysDictData dictData) {
                return new TableDataInfo();
            }


            @Override
            public AjaxResult postDictType(String dictType, String source) {
                return new AjaxResult<>();
            }

            @Override
            public R<String> getDictLabel(String dictType, String dictValue, String source) {
                return R.fail("字典查询失败" + cause);
            }
        };

    }

}
