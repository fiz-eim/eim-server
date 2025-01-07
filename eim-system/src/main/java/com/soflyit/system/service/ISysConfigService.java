package com.soflyit.system.service;

import com.soflyit.system.domain.SysConfig;

import java.util.List;

/**
 * 参数配置 服务层
 *
 * @author soflyit
 */
public interface ISysConfigService {

    SysConfig selectConfigById(Long configId);


    String selectConfigByKey(String configKey);


    List<SysConfig> selectConfigList(SysConfig config);


    int insertConfig(SysConfig config);


    int updateConfig(SysConfig config);


    void deleteConfigByIds(Long[] configIds);


    void loadingConfigCache();


    void clearConfigCache();


    void resetConfigCache();


    String checkConfigKeyUnique(SysConfig config);
}
