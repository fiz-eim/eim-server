package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysDeptUser;
import com.soflyit.system.api.domain.SysUser;

import java.util.List;

/**
 * 用户部门Service接口
 *
 * @author soflyit
 * @date 2022-06-08 17:10:03
 */
public interface ISysDeptUserService {

    SysDeptUser selectSysDeptUserById(Long id);


    List<SysDeptUser> selectSysDeptUserList(SysDeptUser sysDeptUser);


    int insertSysDeptUser(SysDeptUser sysDeptUser);


    int updateSysDeptUser(SysDeptUser sysDeptUser);


    int deleteSysDeptUserByIds(Long[] ids);


    int deleteSysDeptUserById(Long id);


    List<SysDeptUser> selectDeptUsersByUserId(Long userId);


    List<SysUser> getUsersByDeptIdList(List<Long> deptIdList);
}
