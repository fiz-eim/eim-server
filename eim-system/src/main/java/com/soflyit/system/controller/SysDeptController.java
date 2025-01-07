package com.soflyit.system.controller;

import com.soflyit.common.core.constant.UserConstants;
import com.soflyit.common.core.domain.R;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.core.utils.poi.ExcelUtil;
import com.soflyit.common.core.web.controller.BaseController;
import com.soflyit.common.core.web.domain.AjaxResult;
import com.soflyit.common.core.web.page.TableDataInfo;
import com.soflyit.common.security.annotation.InnerAuth;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.domain.SysDept;
import com.soflyit.system.api.domain.SysDeptExport;
import com.soflyit.system.api.domain.vo.SysDeptWithUserCountVo;
import com.soflyit.system.domain.vo.TreeSelect;
import com.soflyit.system.service.ISysDeptService;
import com.soflyit.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门信息
 *
 * @author soflyit
 */
@RestController
@RequestMapping("/dept")
@Api(tags = {"部门管理"})
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService userService;


    @ApiOperation(value = "获取部门列表")
    @GetMapping("/list")
    public AjaxResult<List<SysDept>> list(SysDept dept) {
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    @RequiresPermissions("system:dept:list")
    @ApiOperation(value = "获取部门列表根据Ids")
    @GetMapping("/getDeptList/{ids}")
    public AjaxResult<List<SysDept>> list(@PathVariable Long[] ids) {
        List<SysDept> depts = deptService.getDeptListIds(ids);
        return AjaxResult.success(depts);
    }


    @ApiOperation(value = "获取部门全部列表")
    @GetMapping("/allList")
    public TableDataInfo allList(SysDept dept) {
        startPage();
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> depts = deptService.selectDeptList(dept);
        return getDataTable(depts);
    }


    @RequiresPermissions("system:dept:export")
    @ApiOperation(value = "导出部门数据")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDept dept) {
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> list = deptService.selectDeptList(dept);
        List<SysDeptExport> exportList = list.stream().map(export -> {
            SysDeptExport sysDeptExport = new SysDeptExport();
            BeanUtils.copyProperties(export, sysDeptExport);
            return sysDeptExport;
        }).collect(Collectors.toList());
        ExcelUtil<SysDeptExport> util = new ExcelUtil<SysDeptExport>(SysDeptExport.class);
        util.exportExcel(response, exportList, "部门数据");
    }

    @ApiOperation(value = "获取导入部门模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysDept> util = new ExcelUtil<SysDept>(SysDept.class);
        util.importTemplateExcel(response, "部门数据");
    }

    @RequiresPermissions("system:dept:import")
    @ApiOperation(value = "导入部门数据")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelUtil<SysDept> util = new ExcelUtil<SysDept>(SysDept.class);
            List<SysDept> deptList = util.importExcel(inputStream);
            if (deptList == null) {
                throw new NullPointerException("导入失败! 数据为空");
            }

            Long operId = SecurityUtils.getUserId();
            String message = deptService.importDept(deptList, updateSupport, operId);
            return AjaxResult.success(message);
        } catch (NullPointerException e) {
            throw new NullPointerException("导入失败! 数据格式不正确");
        }
    }


    @RequiresPermissions("system:enterprise:list")
    @ApiOperation(value = "获取企业列表")
    @GetMapping("/listEnterprise")
    public AjaxResult<List<SysDept>> listEnterprise(SysDept dept) {
        List<Byte> orgTypes = new ArrayList<>();
        orgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("orgTypes", orgTypes);
        List<SysDept> depts = deptService.selectEnterpriseList(dept);
        return AjaxResult.success(depts);
    }


    @RequiresPermissions("system:dept:list")
    @ApiOperation(value = "查询部门列表（排除节点）")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult<List<SysDept>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<Byte> exceptOrgTypes = new ArrayList<>();
        SysDept dept = new SysDept();
        dept.setStatus("0");
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> depts = deptService.selectDeptList(dept);
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }


    @RequiresPermissions("system:dept:query")
    @ApiOperation(value = "根据部门编号获取详细信息")
    @GetMapping(value = "/{deptId}")
    public AjaxResult<SysDept> getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }


    @GetMapping("/treeselect")
    @ApiOperation(value = "获取部门下拉树列表")
    public AjaxResult<List<TreeSelect>> treeselect(SysDept dept) {
        dept.setStatus("0");
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }


    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ApiOperation(value = "加载对应角色部门列表树")
    public AjaxResult<List<TreeSelect>> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        List<TreeSelect> tree = deptService.buildDeptTreeSelect(depts);
        AjaxResult ajax = AjaxResult.success(tree);
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", tree);
        return ajax;
    }


    @GetMapping(value = "/postDeptTreeSelect/{postId}")
    @ApiOperation(value = "加载对应岗位部门列表树")
    public AjaxResult<List<TreeSelect>> postDeptTreeSelect(@PathVariable Long postId) {
        SysDept dept = new SysDept();
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> depts = deptService.selectDeptListNoStatus(dept);
        List<TreeSelect> tree = deptService.buildDeptTreeSelect(depts);
        AjaxResult ajax = AjaxResult.success(tree);
        ajax.put("checkedKeys", deptService.selectDeptListByPostId(postId));
        ajax.put("depts", tree);
        return ajax;
    }


    @RequiresPermissions("system:dept:add")
    @ApiOperation(value = "新增部门")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUserId());
        return toAjax(deptService.insertDept(dept));
    }

    @ApiOperation(value = "新增部门")
    @PostMapping("/addDept")
    @InnerAuth
    public AjaxResult addDept(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUserId());
        return toAjax(deptService.insertDept(dept));
    }



    @RequiresPermissions("system:dept:edit")
    @ApiOperation(value = "修改部门")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (deptId.equals(dept.getParentId())) {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUserId());
        return toAjax(deptService.updateDept(dept));
    }


    @RequiresPermissions("system:dept:remove")
    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            String message = "删除部门失败，存在下级部门";
            return AjaxResult.error(message);
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return toAjax(deptService.deleteDeptById(deptId));
    }

    @InnerAuth
    @ApiOperation(value = "根据部门编码数组查询部门信息")
    @PostMapping("/info/byDeptCodes")
    public R<List<SysDept>> getDeptInfoByDeptCodes(@RequestBody List<String> deptCodes) {

        Assert.isTrue(!deptCodes.isEmpty(), "查询数量必须大于0！");

        List<SysDept> depts = deptService.getDeptInfoByDeptCodes(deptCodes);
        return R.ok(depts);
    }

    @InnerAuth
    @ApiOperation(value = "根据部门id数组查询部门信息")
    @PostMapping("/info/byDeptIds")
    public R<List<SysDept>> getDeptInfoByDeptIds(@RequestBody List<Long> deptIds) {

        Assert.isTrue(!deptIds.isEmpty(), "查询数量必须大于0！");

        List<SysDept> depts = deptService.getDeptInfoByDeptIds(deptIds);
        return R.ok(depts);
    }


    @InnerAuth
    @ApiOperation(value = "获取所有部门列表")
    @GetMapping("/info/allDepts")
    public R<List<SysDept>> getAllDeptInfo() {
        SysDept dept = new SysDept();
        dept.setStatus("0");
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDept> depts = deptService.selectDeptList(dept);
        return R.ok(depts);
    }


    @ApiOperation(value = "获取子部门列表")
    @GetMapping("/getChildrenDepts/{deptId}")
    public AjaxResult<List<SysDept>> getChildrenDepts(@PathVariable Long deptId) {
        List<SysDept> children = deptService.selectChildrenDeptById(deptId);
        return AjaxResult.success(children);
    }



    @ApiOperation(value = "获取部门以及部门下员工总数")
    @GetMapping("/getDeptsWithUserCount")
    public AjaxResult<List<SysDeptWithUserCountVo>> getDeptsWithUserCount() {
        SysDept dept = new SysDept();
        dept.setStatus("0");
        List<Byte> exceptOrgTypes = new ArrayList<>();
        exceptOrgTypes.add(Byte.valueOf("3"));
        dept.getParams().put("exceptOrgTypes", exceptOrgTypes);
        List<SysDeptWithUserCountVo> depts = deptService.getDeptsWithUserCount(dept);
        return AjaxResult.success(depts);
    }
}
