package com.soflyit.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soflyit.system.api.domain.SysDeptPost;
import com.soflyit.system.domain.vo.SysDeptPostVo;

import java.util.List;

/**
 * 部门岗位Mapper接口
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
public interface SysDeptPostMapper extends BaseMapper<SysDeptPost> {

    SysDeptPost selectSysDeptPostById(Long id);


    List<SysDeptPost> selectSysDeptPostList(SysDeptPost sysDeptPost);


    int insertSysDeptPost(SysDeptPost sysDeptPost);


    int updateSysDeptPost(SysDeptPost sysDeptPost);


    int deleteSysDeptPostById(Long id);


    int deleteSysDeptPostByIds(Long[] ids);


    int deleteSysDeptPost(SysDeptPost condition);


    List<SysDeptPostVo> getPostsByDeptIds(List<Long> deptIds);


    int deleteByPostIdAndDeptId(SysDeptPost sysDeptPost);
}
