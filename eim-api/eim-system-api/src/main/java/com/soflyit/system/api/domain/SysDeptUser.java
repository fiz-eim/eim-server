package com.soflyit.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户部门对象 sys_dept_user
 *
 * @author soflyit
 * @date 2022-06-08 17:10:03
 */
@TableName(value = "sys_dept_user")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptUser extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "部门ID")
    @TableField(value = "dept_id")
    @ApiModelProperty("部门ID")
    private Long deptId;


    @Excel(name = "用户ID")
    @TableField(value = "user_id")
    @ApiModelProperty("用户ID")
    private Long userId;


}
