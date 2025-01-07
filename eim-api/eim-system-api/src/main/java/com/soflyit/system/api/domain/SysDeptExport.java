package com.soflyit.system.api.domain;

import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.annotation.Excel.Type;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author houzhaole
 * @Date 2022/8/3 13:56
 * @description:
 * @Version 1.0
 */
@Data
public class SysDeptExport extends BaseEntity {
    private static final long serialVersionUID = 1L;


    private Long deptId;


    private Long parentId;


    private String ancestors;


    @Excel(name = "部门编号", type = Type.EXPORT)
    private String deptCode;


    @Excel(name = "部门名称", width = 50)
    private String deptName;


    @ApiModelProperty("显示顺序")
    private String orderNum;


    @Excel(name = "负责人")
    private String leader;


    @Excel(name = "联系电话")
    private String phone;


    @Excel(name = "邮箱")
    private String email;


    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
    private String status;


    private String delFlag;


    @Excel(name = "父部门名称")
    private String parentName;
    @Excel(name = "父部门编码")
    private String parentCode;
    private Integer orgType;
    @Excel(name = "组织类型")
    private String orgName;
    private String divisionCode;
    @Excel(name = "所属区划名称")
    private String divisionName;


    @ApiModelProperty("子部门")
    private List<SysDept> children = new ArrayList<SysDept>();


}
