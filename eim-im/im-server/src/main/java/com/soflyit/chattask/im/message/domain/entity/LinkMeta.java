package com.soflyit.chattask.im.message.domain.entity;

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
 * 链接元数据对象 link_meta
 *
 * @author soflyit
 * @date 2023-11-16 11:05:19
 */
@TableName(value = "link_meta")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkMeta extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id")
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "链接地址")
    @TableField(value = "url")
    @ApiModelProperty("链接地址")
    private String url;


    @Excel(name = "访问时间;访问时间：精确到小时")
    @TableField(value = "access_time")
    @ApiModelProperty("访问时间;访问时间：精确到小时")
    private Long accessTime;


    @Excel(name = "hash值;hash(url+访问时间)")
    @TableField(value = "hash")
    @ApiModelProperty("hash值;hash(url+访问时间)")
    private Long hash;


    @Excel(name = "类型;none-无, image-图片")
    @TableField(value = "type")
    @ApiModelProperty("类型;none-无, image-图片")
    private String type;


    @Excel(name = "元数据")
    @TableField(value = "data")
    @ApiModelProperty("元数据")
    private String data;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;

}
