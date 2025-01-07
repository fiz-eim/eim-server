package com.soflyit.system.mapper;

import com.soflyit.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysUserRoleMapper {

    int deleteUserRoleByUserId(Long userId);


    int deleteUserRole(Long[] ids);


    int countUserRoleByRoleId(Long roleId);


    int batchUserRole(List<SysUserRole> userRoleList);


    int deleteUserRoleInfo(SysUserRole userRole);


    int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
