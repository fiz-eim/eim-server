package com.soflyit.system.mapper;

import com.soflyit.system.domain.SysUserPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户与岗位关联表 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysUserPostMapper {

    int deleteUserPostByUserId(Long userId);


    int countUserPostById(Long postId);


    int deleteUserPost(Long[] ids);


    int batchUserPost(List<SysUserPost> userPostList);


    int deleteAuthUsers(List<SysUserPost> sysUserPosts);


    int deleteAuthUsersOne(SysUserPost sysUserPost);


    List<SysUserPost> selectUserPostByUserId(Long userId);


    List<SysUserPost> selectUserPostByUserNames(@Param("userNames") List<String> userNames);


}
