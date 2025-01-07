package com.soflyit.chattask.dx.modular.cofig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soflyit.chattask.dx.modular.cofig.domain.entity.SysConfigEntity;
import com.soflyit.chattask.dx.modular.cofig.domain.param.SysConfigQueryParam;

import java.util.List;


/**
 * 文档系统级配置(KV)Service接口
 *
 * @author soflyit
 * @date 2023-11-06 15:13:00
 */
public interface SysConfigService extends IService<SysConfigEntity> {
    List<SysConfigEntity> list(SysConfigQueryParam queryParam);
}
