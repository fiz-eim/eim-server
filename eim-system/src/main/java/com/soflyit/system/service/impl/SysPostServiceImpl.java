package com.soflyit.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.exception.ServiceException;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDeptPost;
import com.soflyit.system.api.domain.SysPost;
import com.soflyit.system.api.domain.SysPostRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.param.GetRolePostsQuery;
import com.soflyit.system.domain.SysUserPost;
import com.soflyit.system.mapper.*;
import com.soflyit.system.service.ISysPostService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 岗位信息 服务层处理
 *
 * @author soflyit
 */
@Service
public class SysPostServiceImpl implements ISysPostService {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private SysDeptPostMapper sysDeptPostMapper;

    @Autowired
    private SysPostRoleMapper sysPostRoleMapper;


    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return postMapper.selectPostList(post);
    }

    @Override
    public List<SysPost> listByPostName(SysPost post) {
        return postMapper.listByPostName(post);
    }

    @Override
    public List<SysPost> selectPostListByIds(Long[] ids) {
        return postMapper.selectPostListByIds(ids);
    }


    @Override
    public List<SysPost> selectPostAll() {
        return postMapper.selectPostAll();
    }


    @Override
    public SysPost selectPostById(Long postId) {
        SysPost post = postMapper.selectPostById(postId);

        SysPostRole condition = new SysPostRole();
        condition.setPostId(postId);
        List<SysPostRole> postRoleList = sysPostRoleMapper.selectSysPostRoleList(condition);
        post.setPostRoleList(postRoleList);

        SysDeptPost deptPostCondition = new SysDeptPost();
        deptPostCondition.setPostId(postId);
        List<SysDeptPost> deptPostList = sysDeptPostMapper.selectSysDeptPostList(deptPostCondition);
        post.setDeptPostList(deptPostList);
        return post;
    }


    @Override
    public List<Long> selectPostIdListByUserId(Long userId) {
        return postMapper.selectPostIdListByUserId(userId);
    }


    @Override
    public String checkPostNameUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public String checkPostCodeUnique(SysPost post) {
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public int countUserPostById(Long postId) {
        return userPostMapper.countUserPostById(postId);
    }


    @Override
    public int deletePostById(Long postId) {
        return postMapper.deletePostById(postId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePostByIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }

        for (Long postId : postIds) {

            SysDeptPost condition = new SysDeptPost();
            condition.setPostId(postId);
            sysDeptPostMapper.deleteSysDeptPost(condition);


            SysPostRole roleCondition = new SysPostRole();
            roleCondition.setPostId(postId);
            sysPostRoleMapper.deleteSysPostRole(roleCondition);
        }

        return postMapper.deletePostByIds(postIds);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertPost(SysPost post) {

        int result = postMapper.insertPost(post);
        List<SysDeptPost> deptPostList = post.getDeptPostList();
        List<SysPostRole> postRoleList = post.getPostRoleList();
        if (CollectionUtils.isNotEmpty(deptPostList)) {
            for (SysDeptPost sysDeptPost : deptPostList) {
                sysDeptPost.setPostId(post.getPostId());
                sysDeptPost.setCreateBy(post.getCreateBy());
                sysDeptPost.setCreateTime(post.getCreateTime());
                sysDeptPostMapper.insertSysDeptPost(sysDeptPost);
            }
        }
        if (CollectionUtils.isNotEmpty(postRoleList)) {
            for (SysPostRole sysPostRole : postRoleList) {
                sysPostRole.setPostId(post.getPostId());
                sysPostRole.setCreateBy(post.getCreateBy());
                sysPostRole.setCreateTime(post.getCreateTime());
                sysPostRoleMapper.insertSysPostRole(sysPostRole);
            }
        }

        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePost(SysPost post) {
        List<SysDeptPost> deptPostList = post.getDeptPostList();
        List<SysPostRole> postRoleList = post.getPostRoleList();
        if (CollectionUtils.isNotEmpty(deptPostList)) {
            for (SysDeptPost sysDeptPost : deptPostList) {
                sysDeptPost.setPostId(post.getPostId());
                sysDeptPost.setCreateBy(post.getUpdateBy());
                sysDeptPost.setCreateTime(post.getUpdateTime());
                sysDeptPostMapper.insertSysDeptPost(sysDeptPost);
            }
        }
        if (CollectionUtils.isNotEmpty(postRoleList)) {
            for (SysPostRole sysPostRole : postRoleList) {
                sysPostRole.setPostId(post.getPostId());
                sysPostRole.setCreateBy(post.getCreateBy());
                sysPostRole.setCreateTime(post.getUpdateTime());
                sysPostRoleMapper.insertSysPostRole(sysPostRole);

            }
        }
        List<Long> deletedDeptIds = post.getDeletedDeptPostIds();
        if (CollectionUtils.isNotEmpty(deletedDeptIds)) {
            for (Long ids : deletedDeptIds) {
                SysDeptPost sysDeptPost = new SysDeptPost();
                sysDeptPost.setPostId(post.getPostId());
                sysDeptPost.setDeptId(ids);
                sysDeptPostMapper.deleteByPostIdAndDeptId(sysDeptPost);
            }


        }

        List<Long> deletedRoleIds = post.getDeletedPostRoleIds();
        if (CollectionUtils.isNotEmpty(deletedRoleIds)) {
            sysPostRoleMapper.deleteSysPostRoleByIds(deletedRoleIds.toArray(new Long[deletedRoleIds.size()]));
        }

        return postMapper.updatePost(post);
    }


    @Override
    public int updatePostStatus(SysPost post) {
        SysPost data = new SysPost();
        data.setPostId(post.getPostId());
        data.setStatus(post.getStatus());
        data.setUpdateBy(post.getUpdateBy());
        data.setUpdateTime(post.getUpdateTime());
        return postMapper.updatePost(data);
    }

    @Override
    public List<SysPost> selectPostListByUserId(Long appId, Long userId, Long deptId) {
        SysPost condition = new SysPost();
        Map<String, Object> params = condition.getParams();
        params.put("userId", userId);
        params.put("appId", appId);

        if (deptId != null) {
            params.put("activeDeptId", deptId);
        }
        return postMapper.selectPostListByUserId(condition);
    }


    @Override
    public PageInfo selectPostListByRoleId(GetRolePostsQuery getRolePostsQuery) {
        SysPost condition = new SysPost();
        Map<String, Object> params = condition.getParams();
        if (StringUtils.isNotEmpty(getRolePostsQuery.getPostName())) {
            condition.setPostName(getRolePostsQuery.getPostName());
        }
        params.put("roleId", getRolePostsQuery.getRoleId());
        params.put("appId", getRolePostsQuery.getAppId());
        return new PageInfo(postMapper.selectPostListByRoleId(condition));
    }


    @Override
    public int insertAuthUsers(List<SysUserPost> userPosts) {

        return userPostMapper.batchUserPost(userPosts);
    }


    @Override
    public List<SysUser> selectUnSelectUser(SysUser sysUser) {
        return userMapper.selectUnSelectUser(sysUser);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAuthUsers(List<SysUserPost> userPosts) {
        int flag = 0;
        for (SysUserPost userPost : userPosts) {
            flag += userPostMapper.deleteAuthUsersOne(userPost);
        }
        return flag;
    }


    @Override
    public int deleteAuthUsersOne(SysUserPost sysUserPost) {
        return userPostMapper.deleteAuthUsersOne(sysUserPost);
    }


    @Override
    public List<SysPost> unallocatedPostList(SysPost sysPost) {
        return postMapper.unallocatedPostList(sysPost);
    }


    @Override
    public int insertAuthPosts(Long roleId, Long[] postIds, Long appId) {
        List<SysPostRole> list = new ArrayList<>();
        for (Long postId : postIds) {
            SysPostRole sysPostRole = new SysPostRole();
            sysPostRole.setPostId(postId);
            sysPostRole.setAppId(appId);
            sysPostRole.setRoleId(roleId);
            sysPostRole.setCreateBy(SecurityUtils.getUserId());
            sysPostRole.setCreateTime(new Date());
            list.add(sysPostRole);
        }
        return sysPostRoleMapper.batchPostRole(list);
    }


    @Override
    public List<SysPostRole> selectByAppIdPostId(Long appId, Long postId) {
        return sysPostRoleMapper.selectByAppIdPostId(appId, postId);
    }
}
