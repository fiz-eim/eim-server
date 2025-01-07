package com.soflyit.system.mapper;

import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.vo.SysDeptWithUserCountVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author soflyit
 */
@Repository
public interface SysDeptMapper {

    List<SysDept> selectDeptList(SysDept dept);


    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);


    List<SysDept> isParentContainFilter(SysDept dept);


    List<com.soflyit.system.api.domain.SysDept> sonContainFilterByDeptTab(SysDept dept);


    List<com.soflyit.system.api.domain.SysDept> sonContainFilterByUserTab(SysDept dept);


    SysDept selectDeptById(Long deptId);


    List<SysDept> selectChildrenDeptById(Long deptId);


    int selectNormalChildrenDeptById(Long deptId);


    int hasChildByDeptId(Long deptId);


    int checkDeptExistUser(Long deptId);


    SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);


    int insertDept(SysDept dept);


    int updateDept(SysDept dept);


    void updateDeptStatusNormal(Long[] deptIds);


    int updateDeptChildren(@Param("depts") List<SysDept> depts);


    int deleteDeptById(Long deptId);


    SysDept selectDeptByDeptCode(String deptCode);


    List<SysDept> getAllSubDept(Long DeptId);


    List<SysDept> getFirstSubDept(SysDept dept);


    SysDept getDeptInfoByUserId(Long userId);


    List<SysDept> getMoreDeptInfoByUserId(Long userId);


    List<SysDept> getDeptInfoByDeptCodes(@Param("deptCodes") List<String> deptCodes);


    List<Long> selectDeptListByPostId(@Param("postId") Long postId, @Param("deptCheckStrictly") boolean isDeptCheckStrictly);


    List<SysDept> selectDeptListNoStatus(SysDept dept);


    List<SysDept> getDeptInfoByDeptIds(@Param("deptIds") List<Long> deptIds);


    List<SysDept> selectDeptsByUserName(String userName);

    List<SysDept> getDeptListIds(@Param("ids") Long[] ids);

    List<SysDeptWithUserCountVo> getDeptsWithUserCount(SysDept dept);
}
