package com.soflyit.system.service.impl;

import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.exception.ServiceException;
import com.soflyit.common.core.text.Convert;
import com.soflyit.common.core.utils.SpringUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.bean.BeanValidators;
import com.soflyit.common.datascope.annotation.DataScope;
import com.soflyit.common.redis.service.RedisService;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.SysPost;
import com.soflyit.system.api.domain.SysRole;
import com.soflyit.system.api.domain.SysUser;
import com.soflyit.system.api.domain.vo.SysDeptWithUserCountVo;
import com.soflyit.system.domain.vo.TreeSelect;
import com.soflyit.system.mapper.SysDeptMapper;
import com.soflyit.system.mapper.SysPostMapper;
import com.soflyit.system.mapper.SysRoleMapper;
import com.soflyit.system.service.ISysDeptService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author soflyit
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {
    private static final Logger log = LoggerFactory.getLogger(SysDeptServiceImpl.class);

    @Autowired
    protected Validator validator;
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;


    @Autowired
    private RedisService redisService;
    private static final String deptIdNameKey = "system:depart:name";


    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> getDeptListIds(Long[] ids) {
        return deptMapper.getDeptListIds(ids);
    }

    @Override
    public List<SysDept> selectChildrenDeptById(Long deptId) {
        return deptMapper.selectChildrenDeptById(deptId);
    }


    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectEnterpriseList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }


    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDept dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (SysDept dept : depts) {

            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }


    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }


    @Override
    public List<Long> selectDeptListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }


    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }


    @Override
    public Boolean isParentContainFilter(SysDept dept) {
        List<SysDept> isParentContainFilter = deptMapper.isParentContainFilter(dept);
        return CollectionUtils.isNotEmpty(isParentContainFilter);
    }


    @Override
    public List<com.soflyit.system.api.domain.SysDept> sonContainFilterByDeptTab(SysDept dept) {
        return deptMapper.sonContainFilterByDeptTab(dept);
    }


    @Override
    public List<com.soflyit.system.api.domain.SysDept> sonContainFilterByUserTab(SysDept dept) {
        return deptMapper.sonContainFilterByUserTab(dept);
    }


    @Override
    public int selectNormalChildrenDeptById(Long deptId) {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }


    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }


    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }


    @Override
    public String checkDeptNameUnique(SysDept dept) {
        Long deptId = StringUtils.isNull(dept.getDeptId()) ? -1L : dept.getDeptId();
        if (StringUtils.isNull(dept.getParentId())) {
            dept.setParentId(0L);
        }
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }


    @Override
    public int insertDept(SysDept dept) {
        String name = "部门";
        if (dept.getOrgType() != null && 3 == dept.getOrgType()) {
            name = "企业";
        }
        SysDept parentDept = deptMapper.selectDeptById(dept.getParentId());

        if (parentDept != null && !UserConstants.DEPT_NORMAL.equals(parentDept.getStatus())) {
            throw new ServiceException("上级" + name + "停用，不允许新增");
        }
        SysDept condition = new SysDept();
        condition.setDeptCode(dept.getDeptCode());
        List<SysDept> existDepts = deptMapper.selectDeptList(condition);
        if (CollectionUtils.isNotEmpty(existDepts)) {
            throw new ServiceException("添加" + name + "失败，" + name + "编码已存在");
        }
        if (parentDept != null) {
            dept.setAncestors(parentDept.getAncestors() + "," + dept.getParentId());
            dept.setTanentCode(parentDept.getTanentCode());
        } else {
            dept.setParentId(0L);
            dept.setAncestors(String.valueOf(dept.getParentId()));
        }
        int insertResult = deptMapper.insertDept(dept);
        if (dept.getDeptId() != null && StringUtils.isNotEmpty(dept.getDeptName())) {
            redisService.setCacheMapValue(deptIdNameKey, String.valueOf(dept.getDeptId()), dept.getDeptName());
        }
        return insertResult;
    }


    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        doCheckExist(oldDept, dept);
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        if (dept.getParentId() == null) {
            dept.setParentId(0L);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {

            updateParentDeptStatusNormal(dept);
        }
        return result;
    }


    private void doCheckExist(SysDept oldDept, SysDept dept) {

        if (!StringUtils.equals(oldDept.getDeptCode(), dept.getDeptCode())) {
            SysDept condition = new SysDept();
            condition.setDeptCode(dept.getDeptCode());
            condition.setOrgType(dept.getOrgType());
            List<SysDept> existDepts = deptMapper.selectDeptList(condition);
            if (CollectionUtils.isNotEmpty(existDepts)) {
                throw new ServiceException("修改部门失败，部门编码已存在");
            }
        }

    }


    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        deptMapper.updateDeptStatusNormal(deptIds);
    }


    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (!children.isEmpty()) {
            deptMapper.updateDeptChildren(children);
        }
    }


    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    @Override
    public SysDept selectDeptByDeptCode(String deptCode) {
        return deptMapper.selectDeptByDeptCode(deptCode);
    }


    private void recursionFn(List<SysDept> list, SysDept t) {

        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }


    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext()) {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }


    private boolean hasChild(List<SysDept> list, SysDept t) {
        return !getChildList(list, t).isEmpty() ? true : false;
    }


    @Override
    public List<SysDept> getAllSubDept(Long DeptId) {
        return deptMapper.getAllSubDept(DeptId);
    }


    @Override
    public List<SysDept> getFirstSubDept(SysDept dept) {
        return deptMapper.getFirstSubDept(dept);
    }


    @Override
    public SysDept getDeptInfoByUserId(Long userId) {
        return deptMapper.getDeptInfoByUserId(userId);

    }


    @Override
    public List<SysDept> getMoreDeptInfoByUserId(Long userId) {
        return deptMapper.getMoreDeptInfoByUserId(userId);
    }

    @Override
    public List<SysDept> getDeptInfoByDeptCodes(List<String> deptCodes) {
        return deptMapper.getDeptInfoByDeptCodes(deptCodes);
    }


    @Override
    public List<Long> selectDeptListByPostId(Long postId) {
        SysPost sysPost = postMapper.selectPostById(postId);
        return deptMapper.selectDeptListByPostId(postId, sysPost.isDeptCheckStrictly());
    }


    @Override
    public List<SysDept> selectDeptListNoStatus(SysDept dept) {
        return deptMapper.selectDeptListNoStatus(dept);
    }

    @Override
    public List<SysDept> getDeptInfoByDeptIds(List<Long> deptIds) {
        return deptMapper.getDeptInfoByDeptIds(deptIds);
    }


    @Override
    public String importDept(List<SysDept> deptList, boolean updateSupport, Long operId) {
        if (StringUtils.isNull(deptList) || deptList.isEmpty()) {
            throw new ServiceException("导入部门数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        int cycle = 0;
        for (SysDept dept : deptList) {
            cycle++;

            dept.setOrderNum("0");
            dept.setStatus("0");
            try {
                SysDept condition = new SysDept();
                List<SysDept> d = new ArrayList<>();

                SysDept parentDept = new SysDept();

                parentDept.setPhone("1");
                if (dept.getParentCode() != "") {
                    parentDept = deptMapper.selectDeptByDeptCode(dept.getParentCode());
                    if (StringUtils.isNotNull(parentDept)) {
                        dept.setParentId(parentDept.getDeptId());
                        dept.setTanentCode(parentDept.getTanentCode());
                    }
                }

                condition.setDeptCode(dept.getDeptCode());
                condition.getParams();
                d = deptMapper.selectDeptList(condition);

                if (CollectionUtils.isEmpty(d) && StringUtils.isNotNull(parentDept) && dept.getDeptCode() != "" && dept.getDeptName() != "") {
                    if (UserConstants.NOT_UNIQUE.equals(this.checkDeptNameUnique(dept))) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、新增部门 " + dept.getDeptName() + "'失败，部门名称已存在");
                    } else {
                        this.insertDept(dept);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、部门 " + dept.getDeptName() + " 导入成功");
                    }
                } else if (updateSupport && StringUtils.isNotNull(parentDept) && dept.getDeptCode() != "" && dept.getDeptName() != "") {
                    BeanValidators.validateWithException(validator, dept);
                    SysDept sysDept = deptMapper.selectDeptByDeptCode(dept.getDeptCode());
                    if (UserConstants.NOT_UNIQUE.equals(this.checkDeptNameUnique(sysDept))) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、修改部门 " + dept.getDeptName() + "'失败，部门名称已存在");
                    } else if (sysDept.getDeptId().equals(dept.getParentId())) {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、修改部门 " + dept.getDeptName() + "'失败，上级部门不能是自己");
                    } else {
                        dept.setDeptId(sysDept.getDeptId());
                        dept.setUpdateBy(operId);
                        deptMapper.updateDept(dept);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、部门 " + dept.getDeptName() + " 更新成功");
                    }
                } else if (StringUtils.isNull(parentDept)) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、部门 " + dept.getDeptName() + " 的父级部门不存在");
                } else if (dept.getDeptCode() == "") {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、第" + cycle + "条数据部门编码为空");
                } else if (dept.getDeptName() == "") {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、第" + cycle + "条数据部门名称为空");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、部门编码 " + dept.getDeptCode() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、部门 " + dept.getDeptName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public List<SysDeptWithUserCountVo> getDeptsWithUserCount(SysDept dept) {
        return deptMapper.getDeptsWithUserCount(dept);
    }

    @PostConstruct
    private void initDeptIdName() {
        SysDept condition = new SysDept();
        List<SysDept> sysUserList = deptMapper.selectDeptList(condition);
        Map<String, String> deptIdNameMap = sysUserList.stream().collect(Collectors.toMap(key -> String.valueOf(key.getDeptId()), SysDept::getDeptName));
        redisService.setCacheMap(deptIdNameKey, deptIdNameMap);
    }
}
