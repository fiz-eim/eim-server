package com.soflyit.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 部门岗位对象 sys_dept_post
 *
 * @author soflyit
 * @date 2022-05-26 10:35:50
 */
@TableName(value = "sys_dept_post")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptPost extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "部门Id")
    @TableField(value = "dept_id")
    @ApiModelProperty("部门Id")
    private Long deptId;


    @Excel(name = "岗位Id")
    @TableField(value = "post_id")
    @ApiModelProperty("岗位Id")
    private Long postId;


    @Excel(name = "创建人")
    @TableField(exist = false)
    @ApiModelProperty("创建人")
    private String createUser;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @Excel(name = "更新人")
    @TableField(exist = false)
    @ApiModelProperty("更新人")
    private String updateUser;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime;

}
