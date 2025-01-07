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
 * 密码设置对象 sys_password
 *
 * @author soflyit
 * @date 2023-04-24 11:25:34
 */
@TableName(value = "sys_password")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPassword extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "是否单一登录")
    @TableField(value = "single_login")
    @ApiModelProperty("是否单一登录：0-否；1-是，默认0")
    private Integer singleLogin;


    @Excel(name = "踢出类型")
    @TableField(value = "kick_type")
    @ApiModelProperty("踢出类型：0-后登录踢出先登录；1-已登录禁止再登录；默认0")
    private Integer kickType;


    @Excel(name = "是否密码强度限制")
    @TableField(value = "strength_limit")
    @ApiModelProperty("是否密码强度限制：0-否；1-是，默认0")
    private Integer strengthLimit;


    @Excel(name = "是否需要最小长度限制")
    @TableField(value = "need_length")
    @ApiModelProperty("是否需要最小长度限制：0-否；1-是，默认0")
    private Integer needLength;


    @Excel(name = "最小长度")
    @TableField(value = "min_length")
    @ApiModelProperty("最小长度")
    private Integer minLength;


    @Excel(name = "包含数字：0-否")
    @TableField(value = "need_digit")
    @ApiModelProperty("包含数字：0-否；1-是，默认0")
    private Integer needDigit;


    @Excel(name = "包含小写字母")
    @TableField(value = "need_lower_case")
    @ApiModelProperty("包含小写字母：0-否；1-是，默认0")
    private Integer needLowerCase;


    @Excel(name = "包含大写字母")
    @TableField(value = "need_upper_case")
    @ApiModelProperty("包含大写字母：0-否；1-是，默认0")
    private Integer needUpperCase;


    @Excel(name = "包含特殊字符")
    @TableField(value = "need_special_character")
    @ApiModelProperty("包含特殊字符：0-否；1-是，默认0")
    private Integer needSpecialCharacter;


    @Excel(name = "不包含用户名")
    @TableField(value = "not_user_name")
    @ApiModelProperty("不包含用户名：0-否；1-是，默认0")
    private Integer notUserName;


    @Excel(name = "初始密码强制修改")
    @TableField(value = "force_change")
    @ApiModelProperty("初始密码强制修改：0-否；1-是，默认0")
    private Integer forceChange;


    @Excel(name = "密码错误次数")
    @TableField(value = "limit_failed_count")
    @ApiModelProperty("密码错误次数：0-不限制；取值范围0-10，默认-5")
    private Integer limitFailedCount;

}
