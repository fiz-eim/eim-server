package com.soflyit.chattask.dx.modular.cofig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soflyit.chattask.dx.modular.cofig.domain.entity.SysConfigEntity;
import com.soflyit.chattask.dx.modular.cofig.domain.param.SysConfigQueryParam;
import com.soflyit.chattask.dx.modular.cofig.mapper.SysConfigMapper;
import com.soflyit.chattask.dx.modular.cofig.service.SysConfigService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.cofig.domain
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-09  19:36
 * @Description: xxxVO 命名为响应数据的封装，为前端页面提供数据结构。
 * @Version: 1.0
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements SysConfigService {

    @Override
    public List<SysConfigEntity> list(SysConfigQueryParam queryParam) {
        LambdaQueryWrapper<SysConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtils.isNotEmpty(queryParam.getConfigKey().getFieldValue())) {
            queryWrapper.eq(SysConfigEntity::getConfigKey, queryParam.getConfigKey().getFieldValue());
        }

        if (ObjectUtils.isNotEmpty(queryParam.getConfigKey().getOrderType())) {
            if (queryParam.getConfigKey().getOrderType() == 0) {
                queryWrapper.orderBy(true, true, SysConfigEntity::getConfigKey);
            } else if (queryParam.getConfigKey().getOrderType() == 1) {
                queryWrapper.orderBy(true, false, SysConfigEntity::getConfigKey);
            }
        }
        return this.list(queryWrapper);
    }
}
