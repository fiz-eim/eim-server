package com.soflyit.system.api.domain;

import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门表 sys_dept
 *
 * @author soflyit
 */
@ApiModel
@Data
public class SysDept extends BaseEntity {
    private static final long serialVersionUID = 1L;


    private Long userId;

    @ApiModelProperty("部门Id")
    private Long deptId;
    @ApiModelProperty("租户code")
    private String tanentCode;

    @ApiModelProperty("父部门ID")
    private Long parentId;


    @ApiModelProperty("祖级列表")
    private String ancestors;


    @ApiModelProperty("部门编码")
    @Excel(name = "部门编码(必填)")
    private String deptCode;


    @ApiModelProperty("部门名称")
    @Excel(name = "部门名称(必填)")
    private String deptName;


    @ApiModelProperty("显示顺序")
    private String orderNum;


    @ApiModelProperty("负责人")
    @Excel(name = "负责人")
    private String leader;


    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String phone;


    @ApiModelProperty("邮箱")
    @Excel(name = "用户邮箱")
    private String email;


    @ApiModelProperty(value = "部门状态", allowableValues = "0-正常, 1-停用", example = "0")
    private String status;


    @ApiModelProperty(value = "删除标志", allowableValues = "0-正常, 2-删除", example = "0")
    private String delFlag;


    @ApiModelProperty("父部门名称")
    private String parentName;
    @Excel(name = "父部门编码")
    private String parentCode;
    @ApiModelProperty("组织类型")
    private Integer orgType;
    @Excel(name = "组织类型", prompt = "对应字典“组织类型”的“字典标签”列的值")
    private String orgName;
    @ApiModelProperty("所属区划编码")
    private String divisionCode;
    @Excel(name = "所属区划", prompt = "对应菜单“行政划分”的“行政名称”列的值")
    private String divisionName;

    @ApiModelProperty("子部门")
    private List<SysDept> children = new ArrayList<SysDept>();

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @NotBlank(message = "显示顺序不能为空")
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<SysDept> getChildren() {
        return children;
    }

    public void setChildren(List<SysDept> children) {
        this.children = children;
    }

    public String getTanentCode() {
        return tanentCode;
    }

    public void setTanentCode(String tanentCode) {
        this.tanentCode = tanentCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("deptId", getDeptId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("deptName", getDeptName())
                .append("orderNum", getOrderNum())
                .append("leader", getLeader())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
