package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysDeptPost;
import com.soflyit.system.domain.vo.SysDeptPostVo;

import java.util.List;

/**
 * 部门岗位Service接口
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
public interface ISysDeptPostService {

    SysDeptPost selectSysDeptPostById(Long id);


    List<SysDeptPost> selectSysDeptPostList(SysDeptPost sysDeptPost);


    int insertSysDeptPost(SysDeptPost sysDeptPost);


    int updateSysDeptPost(SysDeptPost sysDeptPost);


    int deleteSysDeptPostByIds(Long[] ids);


    int deleteSysDeptPostById(Long id);



    List<SysDeptPostVo> getPostsByDeptIds(List<Long> deptIds);
}
