package com.soflyit.system.service.impl;

import com.soflyit.system.api.domain.SysDeptPost;
import com.soflyit.system.domain.vo.SysDeptPostVo;
import com.soflyit.system.mapper.SysDeptPostMapper;
import com.soflyit.system.service.ISysDeptPostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门岗位Service业务层处理
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
@Service
public class SysDeptPostServiceImpl implements ISysDeptPostService {
    @Resource
    private SysDeptPostMapper sysDeptPostMapper;


    @Override
    public SysDeptPost selectSysDeptPostById(Long id) {
        return sysDeptPostMapper.selectSysDeptPostById(id);
    }


    @Override
    public List<SysDeptPost> selectSysDeptPostList(SysDeptPost sysDeptPost) {
        return sysDeptPostMapper.selectSysDeptPostList(sysDeptPost);
    }


    @Override
    public int insertSysDeptPost(SysDeptPost sysDeptPost) {
        return sysDeptPostMapper.insertSysDeptPost(sysDeptPost);
    }


    @Override
    public int updateSysDeptPost(SysDeptPost sysDeptPost) {
        return sysDeptPostMapper.updateSysDeptPost(sysDeptPost);
    }


    @Override
    public int deleteSysDeptPostByIds(Long[] ids) {
        return sysDeptPostMapper.deleteSysDeptPostByIds(ids);
    }


    @Override
    public int deleteSysDeptPostById(Long id) {
        return sysDeptPostMapper.deleteSysDeptPostById(id);
    }


    @Override
    public List<SysDeptPostVo> getPostsByDeptIds(List<Long> deptIds) {
        return sysDeptPostMapper.getPostsByDeptIds(deptIds);
    }
}
