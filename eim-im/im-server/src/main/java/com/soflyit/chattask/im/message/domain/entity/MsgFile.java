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

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * 消息文件对象 msg_file
 *
 * @author soflyit
 * @date 2023-11-16 11:05:18
 */
@TableName(value = "msg_file")
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
public class MsgFile extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type=ASSIGN_ID)
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "消息Id")
    @TableField(value = "msg_id")
    @ApiModelProperty("消息Id")
    private Long msgId;


    @Excel(name = "频道Id")
    @TableField(value = "channel_id")
    @ApiModelProperty("频道Id")
    private Long channelId;


    @Excel(name = "文件名")
    @TableField(value = "name")
    @ApiModelProperty("文件名")
    private String name;


    @Excel(name = "存储类型;1-本地存在,2-文档库")
    @TableField(value = "storage_type")
    @ApiModelProperty("存储类型;1-本地存在,2-文档库")
    private Short storageType;


    @Excel(name = "外部系统Id")
    @TableField(value = "external_id")
    @ApiModelProperty("外部系统Id")
    private String externalId;


    @Excel(name = "文件路径")
    @TableField(value = "path")
    @ApiModelProperty("文件路径")
    private String path;


    @Excel(name = "缩略图路径")
    @TableField(value = "thumbnail_path")
    @ApiModelProperty("缩略图路径")
    private String thumbnailPath;


    @Excel(name = "预览路径")
    @TableField(value = "preview_path")
    @ApiModelProperty("预览路径")
    private String previewPath;


    @Excel(name = "扩展名")
    @TableField(value = "extension")
    @ApiModelProperty("扩展名")
    private String extension;


    @Excel(name = "文件类型")
    @TableField(value = "mime_type")
    @ApiModelProperty("文件类型")
    private String mimeType;


    @Excel(name = "文件大小")
    @TableField(value = "size")
    @ApiModelProperty("文件大小")
    private Long size;


    @Excel(name = "宽度")
    @TableField(value = "width")
    @ApiModelProperty("宽度")
    private Long width;


    @Excel(name = "高度")
    @TableField(value = "height")
    @ApiModelProperty("高度")
    private Long height;


    @Excel(name = "是否有预览图片")
    @TableField(value = "has_preview_image")
    @ApiModelProperty("是否有预览图片")
    private Short hasPreviewImage;


    @Excel(name = "迷你预览图")
    @TableField(value = "mini_preview")
    @ApiModelProperty("迷你预览图")
    private String miniPreview;


    @Excel(name = "文件哈希值")
    @TableField(value = "hash_sha256")
    @ApiModelProperty("文件哈希值")
    private String hashSha256;


    @Excel(name = "文件哈希值")
    @TableField(value = "hash_sha1")
    @ApiModelProperty("文件哈希值")
    private String hashSha1;


    @Excel(name = "文件哈希值")
    @TableField(value = "hash_md5")
    @ApiModelProperty("文件哈希值")
    private String hashMd5;


    @TableField(value = "ext_data")
    @ApiModelProperty("扩展信息")
    private String extData;


    @Excel(name = "是否已归档;1-是,2-否")
    @TableField(value = "archived")
    @ApiModelProperty("是否已归档;1-是,2-否")
    private Short archived;


    @TableField(value = "uuid")
    @ApiModelProperty("前端文件Id")
    private String uuid;


    @Excel(name = "删除人")
    @TableField(value = "delete_by")
    @ApiModelProperty("删除人")
    private Long deleteBy;


    @Excel(name = "删除时间;默认-1，表示未删除")
    @TableField(value = "delete_time")
    @ApiModelProperty("删除时间;默认-1，表示未删除")
    private Long deleteTime;


    @Excel(name = "租户号")
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户号")
    private Long tenantId;


    @Excel(name = "乐观锁")
    @TableField(value = "REVISION")
    @ApiModelProperty("乐观锁")
    private Long revision;


    @TableField(exist = false)
    @ApiModelProperty("文件key")
    private String fileKey;

}
