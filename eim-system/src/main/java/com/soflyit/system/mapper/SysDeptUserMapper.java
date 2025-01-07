package com.soflyit.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.system.api.domain.SysDeptUser;
import com.soflyit.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户部门Mapper接口
 *
 * @author soflyit
 * @date 2022-06-08 17:10:03
 */
public interface SysDeptUserMapper extends BaseMapper<SysDeptUser> {

    SysDeptUser selectSysDeptUserById(Long id);


    List<SysDeptUser> selectSysDeptUserList(SysDeptUser sysDeptUser);


    int insertSysDeptUser(SysDeptUser sysDeptUser);


    int updateSysDeptUser(SysDeptUser sysDeptUser);


    int deleteSysDeptUserById(Long id);


    int deleteSysDeptUserByIds(Long[] ids);


    int batchDeptUser(List<SysDeptUser> deptUsers);


    int deleteSysDeptUserByUserId(Long userId);


    List<SysDeptUser> selectDeptUsersByUserId(Long userId);


    int deleteByDeptIdUserId(SysDeptUser sysDeptUser);


    List<SysUser> selectUserListByDeptIdList(@Param("deptIdList") List<Long> deptIdList);
}
