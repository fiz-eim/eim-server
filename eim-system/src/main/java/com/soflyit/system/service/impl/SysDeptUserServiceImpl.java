package com.soflyit.system.service.impl;

import com.soflyit.common.core.utils.DateUtils;
import com.soflyit.system.api.domain.SysDeptUser;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.mapper.SysDeptUserMapper;
import com.soflyit.system.service.ISysDeptUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户部门Service业务层处理
 *
 * @author soflyit
 * @date 2022-06-08 17:10:03
 */
@Service
public class SysDeptUserServiceImpl implements ISysDeptUserService {
    @Resource
    private SysDeptUserMapper sysDeptUserMapper;


    @Override
    public SysDeptUser selectSysDeptUserById(Long id) {
        return sysDeptUserMapper.selectSysDeptUserById(id);
    }


    @Override
    public List<SysDeptUser> selectSysDeptUserList(SysDeptUser sysDeptUser) {
        return sysDeptUserMapper.selectSysDeptUserList(sysDeptUser);
    }


    @Override
    public int insertSysDeptUser(SysDeptUser sysDeptUser) {
        sysDeptUser.setCreateTime(DateUtils.getNowDate());
        return sysDeptUserMapper.insertSysDeptUser(sysDeptUser);
    }


    @Override
    public int updateSysDeptUser(SysDeptUser sysDeptUser) {
        sysDeptUser.setUpdateTime(DateUtils.getNowDate());
        return sysDeptUserMapper.updateSysDeptUser(sysDeptUser);
    }


    @Override
    public int deleteSysDeptUserByIds(Long[] ids) {
        return sysDeptUserMapper.deleteSysDeptUserByIds(ids);
    }


    @Override
    public int deleteSysDeptUserById(Long id) {
        return sysDeptUserMapper.deleteSysDeptUserById(id);
    }


    @Override
    public List<SysDeptUser> selectDeptUsersByUserId(Long userId) {
        return sysDeptUserMapper.selectDeptUsersByUserId(userId);
    }

    @Override
    public List<SysUser> getUsersByDeptIdList(List<Long> deptIdList) {
        return sysDeptUserMapper.selectUserListByDeptIdList(deptIdList);
    }
}
