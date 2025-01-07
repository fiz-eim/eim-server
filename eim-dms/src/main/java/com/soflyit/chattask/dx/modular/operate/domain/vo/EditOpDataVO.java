package com.soflyit.chattask.dx.modular.operate.domain.vo;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.operate.domain.vo
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-17  22:39
 * @Description: 
 * @Version: 1.0
 */
@Data
public class EditOpDataVO extends BaseOpDataVO {
    private String per = "";
    private String reName;
    private String oldName;


    @Override
    public String desc() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.getUserId());
        buffer.append(this.per);
        buffer.append(this.getUserId());
        buffer.append(this.getOpTypeVO().getOpName());

        return buffer.toString();
    }
}
