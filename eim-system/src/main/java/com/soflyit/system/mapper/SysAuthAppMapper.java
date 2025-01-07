package com.soflyit.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.system.api.domain.SysAuthApp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 应用Mapper接口
 *
 * @author soflyit
 * @date 2022-05-14
 */
@Repository
public interface SysAuthAppMapper extends BaseMapper<SysAuthApp> {

    SysAuthApp selectSysAuthAppById(Long id);


    List<SysAuthApp> selectSysAuthAppList(SysAuthApp sysAuthApp);


    int insertSysAuthApp(SysAuthApp sysAuthApp);


    int updateSysAuthApp(SysAuthApp sysAuthApp);


    int deleteSysAuthAppById(Long id);


    int deleteSysAuthAppByIds(Long[] ids);


    List<SysAuthApp> selectAppList(SysAuthApp sysAuthApp);
}
