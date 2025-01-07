package com.soflyit.system.mapper;

import com.soflyit.system.domain.SysConfig;

import java.util.List;

/**
 * 参数配置 数据层
 *
 * @author soflyit
 */
public interface SysConfigMapper {

    SysConfig selectConfig(SysConfig config);


    List<SysConfig> selectConfigList(SysConfig config);


    SysConfig checkConfigKeyUnique(String configKey);


    int insertConfig(SysConfig config);


    int updateConfig(SysConfig config);


    int deleteConfigById(Long configId);


    int deleteConfigByIds(Long[] configIds);
}
