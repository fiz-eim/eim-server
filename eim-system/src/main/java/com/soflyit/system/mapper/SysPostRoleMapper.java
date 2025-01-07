package com.soflyit.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.system.api.domain.SysPostRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位角色Mapper接口
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
public interface SysPostRoleMapper extends BaseMapper<SysPostRole> {

    SysPostRole selectSysPostRoleById(Long id);


    List<SysPostRole> selectSysPostRoleList(SysPostRole sysPostRole);


    int insertSysPostRole(SysPostRole sysPostRole);


    int updateSysPostRole(SysPostRole sysPostRole);


    int deleteSysPostRoleById(Long id);


    int deleteSysPostRoleByIds(Long[] ids);


    int deleteSysPostRole(SysPostRole sysPostRole);


    int deletePostRoleInfo(SysPostRole sysPostRole);


    int deletePostRoleInfos(@Param("roleId") Long roleId, @Param("postIds") Long[] postIds);


    int batchPostRole(List<SysPostRole> list);


    int countPostRoleById(Long postId);


    int countPostRoleByRoleId(Long roleId);


    List<SysPostRole> selectByAppIdPostId(@Param("appId") Long appId, @Param("postId") Long postId);
}
