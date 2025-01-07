package com.soflyit.system.service;

import com.github.pagehelper.PageInfo;
import com.soflyit.system.api.domain.SysPost;
import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.param.GetRolePostsQuery;
import com.soflyit.system.domain.SysUserPost;

import java.util.List;

/**
 * 岗位信息 服务层
 *
 * @author soflyit
 */
public interface ISysPostService {

    List<SysPost> selectPostList(SysPost post);


    List<SysPost> selectPostAll();


    SysPost selectPostById(Long postId);


    List<Long> selectPostIdListByUserId(Long userId);


    String checkPostNameUnique(SysPost post);


    String checkPostCodeUnique(SysPost post);


    int countUserPostById(Long postId);


    int deletePostById(Long postId);


    int deletePostByIds(Long[] postIds);


    int insertPost(SysPost post);


    int updatePost(SysPost post);


    int updatePostStatus(SysPost post);


    List<SysPost> selectPostListByUserId(Long userId, Long appId, Long deptId);


    PageInfo selectPostListByRoleId(GetRolePostsQuery getRolePostsQuery);


    int insertAuthUsers(List<SysUserPost> userPosts);


    List<SysUser> selectUnSelectUser(SysUser sysUser);


    int deleteAuthUsers(List<SysUserPost> userPosts);


    int deleteAuthUsersOne(SysUserPost sysUserPost);


    List<SysPost> unallocatedPostList(SysPost sysPost);


    int insertAuthPosts(Long roleId, Long[] postIds, Long appId);


    List<SysPostRole> selectByAppIdPostId(Long appId, Long postId);

    List<SysPost> selectPostListByIds(Long[] ids);

    List<SysPost> listByPostName(SysPost post);
}
