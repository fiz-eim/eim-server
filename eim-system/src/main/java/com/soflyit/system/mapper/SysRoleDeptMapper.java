package com.soflyit.system.mapper;

import com.soflyit.system.domain.SysRoleDept;

import java.util.List;

/**
 * 角色与部门关联表 数据层
 *
 * @author soflyit
 */
public interface SysRoleDeptMapper {

    int deleteRoleDeptByRoleId(Long roleId);


    int deleteRoleDept(Long[] ids);


    int selectCountRoleDeptByDeptId(Long deptId);


    int batchRoleDept(List<SysRoleDept> roleDeptList);
}
