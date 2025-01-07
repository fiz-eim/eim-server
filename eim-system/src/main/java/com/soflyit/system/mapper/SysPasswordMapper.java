package com.soflyit.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.system.api.domain.SysPassword;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 密码设置Mapper接口
 *
 * @author soflyit
 * @date 2023-04-24 11:25:34
 */
@Repository
public interface SysPasswordMapper extends BaseMapper<SysPassword> {

    SysPassword selectSysPasswordById(Long id);


    List<SysPassword> selectSysPasswordList(SysPassword sysPassword);


    int insertSysPassword(SysPassword sysPassword);


    int updateSysPassword(SysPassword sysPassword);


    int deleteSysPasswordById(Long id);


    int deleteSysPasswordByIds(Long[] ids);
}
