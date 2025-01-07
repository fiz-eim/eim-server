package com.soflyit.system.service.impl;

import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.mapper.SysPostRoleMapper;
import com.soflyit.system.service.ISysPostRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位角色Service业务层处理
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
@Service
public class SysPostRoleServiceImpl implements ISysPostRoleService {
    @Autowired
    private SysPostRoleMapper sysPostRoleMapper;


    @Override
    public SysPostRole selectSysPostRoleById(Long id) {
        return sysPostRoleMapper.selectSysPostRoleById(id);
    }


    @Override
    public List<SysPostRole> selectSysPostRoleList(SysPostRole sysPostRole) {
        return sysPostRoleMapper.selectSysPostRoleList(sysPostRole);
    }


    @Override
    public int insertSysPostRole(SysPostRole sysPostRole) {
        return sysPostRoleMapper.insertSysPostRole(sysPostRole);
    }


    @Override
    public int updateSysPostRole(SysPostRole sysPostRole) {
        return sysPostRoleMapper.updateSysPostRole(sysPostRole);
    }


    @Override
    public int deleteSysPostRoleByIds(Long[] ids) {
        return sysPostRoleMapper.deleteSysPostRoleByIds(ids);
    }


    @Override
    public int deleteSysPostRoleById(Long id) {
        return sysPostRoleMapper.deleteSysPostRoleById(id);
    }
}
