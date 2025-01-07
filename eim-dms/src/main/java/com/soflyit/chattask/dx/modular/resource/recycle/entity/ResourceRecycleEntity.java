package com.soflyit.chattask.dx.modular.resource.recycle.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.soflyit.chattask.dx.common.base.DmsCommonEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * (ResourceRecycle)实体类
 *
 * @author JiangNing.G
 * @author JiangNing.G
 * @since 2023-11-18 11:48:07
 */
@TableName(value = "dx_resource_recycle")
@ApiModel
@Data
public class ResourceRecycleEntity extends DmsCommonEntity {


    private Long id;


    private Long resourceId;


    private Long folderParentId;


    private String remark;




}

