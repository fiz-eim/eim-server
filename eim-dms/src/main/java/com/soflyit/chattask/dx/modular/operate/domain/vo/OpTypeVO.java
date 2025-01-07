package com.soflyit.chattask.dx.modular.operate.domain.vo;

import com.soflyit.chattask.dx.modular.operate.domain.enums.OpTypeEnum;
import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.operate.domain.vo
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-17  22:42
 * @Description: 操作记录
 * @Version: 1.0
 */
@Data
public class OpTypeVO {

    private String opType;


    private String opName;

    private OpTypeVO(String opName, String opType) {
        this.opName = opName;
        this.opType = opType;
    }


    public static OpTypeVO instance(OpTypeEnum typeEnum) {
        return new OpTypeVO(typeEnum.getName(), typeEnum.getCode());
    }


}