package com.soflyit.system.service;

import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.vo.SysDeptWithUserCountVo;
import com.soflyit.system.domain.vo.TreeSelect;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author soflyit
 */
public interface ISysDeptService {

    List<SysDept> selectDeptList(SysDept dept);


    List<SysDept> selectEnterpriseList(SysDept dept);


    List<SysDept> buildDeptTree(List<SysDept> depts);


    List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);


    List<Long> selectDeptListByRoleId(Long roleId);


    SysDept selectDeptById(Long deptId);


    Boolean isParentContainFilter(SysDept dept);


    List<com.soflyit.system.api.domain.SysDept> sonContainFilterByDeptTab(SysDept dept);


    List<com.soflyit.system.api.domain.SysDept> sonContainFilterByUserTab(SysDept dept);


    int selectNormalChildrenDeptById(Long deptId);


    boolean hasChildByDeptId(Long deptId);


    boolean checkDeptExistUser(Long deptId);


    String checkDeptNameUnique(SysDept dept);


    void checkDeptDataScope(Long deptId);


    int insertDept(SysDept dept);


    int updateDept(SysDept dept);


    int deleteDeptById(Long deptId);


    SysDept selectDeptByDeptCode(String deptCode);


    List<SysDept> getAllSubDept(Long DeptId);


    List<SysDept> getFirstSubDept(SysDept dept);


    SysDept getDeptInfoByUserId(Long userId);


    List<SysDept> getMoreDeptInfoByUserId(Long userId);


    List<SysDept> getDeptInfoByDeptCodes(List<String> deptCodes);


    List<Long> selectDeptListByPostId(Long postId);


    List<SysDept> selectDeptListNoStatus(SysDept dept);


    List<SysDept> getDeptInfoByDeptIds(List<Long> deptIds);


    String importDept(List<SysDept> deptList, boolean updateSupport, Long operId);

    List<SysDept> getDeptListIds(Long[] ids);

    List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * 获取部门以及部门下员工总数
     **/
    List<SysDeptWithUserCountVo> getDeptsWithUserCount(SysDept dept);
}
