package com.soflyit.system.mapper;

import com.soflyit.system.api.domain.SysPost;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysPostMapper {

    List<SysPost> selectPostList(SysPost post);


    List<SysPost> selectPostAll();


    SysPost selectPostById(Long postId);


    List<Long> selectPostIdListByUserId(Long userId);


    List<SysPost> selectPostListByUserId(SysPost post);


    List<SysPost> selectPostsByUserName(String userName);


    int deletePostById(Long postId);


    int deletePostByIds(Long[] postIds);


    int updatePost(SysPost post);


    int insertPost(SysPost post);


    SysPost checkPostNameUnique(String postName);


    SysPost checkPostCodeUnique(String postCode);



    List<SysPost> selectPostListByRoleId(SysPost condition);


    List<SysPost> unallocatedPostList(SysPost sysPost);

    List<SysPost> selectPostListByIds(Long[] ids);

    List<SysPost> listByPostName(SysPost post);
}
