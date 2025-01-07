package com.soflyit.chattask.dx.modular.resource.resource.domain.vo;

import com.soflyit.chattask.dx.modular.operate.domain.entity.OperateRecordEntity;
import lombok.Data;

import java.util.List;

/**
 * Package: com.soflyit.chattask.dx.modular.resource.resource.domain.vo
 *
 * @Description: 点击文件/文件夹时返回的详细信息
 * @date: 2023/11/28 16:08
 * @author: dddgoal@163.com
 */
@Data
public class ResourceDetailVo {

    private ResourceVO baseInfo;


    private List<OperateRecordEntity> operateRecord;


}
