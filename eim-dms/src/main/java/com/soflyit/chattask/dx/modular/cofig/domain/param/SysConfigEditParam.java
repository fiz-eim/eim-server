package com.soflyit.chattask.dx.modular.cofig.domain.param;

import lombok.Data;

/**
 * @BelongsProject: chat-dx-manager
 * @BelongsPackage: com.soflyit.chattask.dx.modular.cofig.domain
 * @Author: JiangNing.G
 * @CreateTime: 2023-11-09  19:36
 * @Description: xxxParam 命名为API接口参数封装，为接收前端参数数据结构。
 * @Version: 1.0
 */
@Data
public class SysConfigEditParam extends SysConfigAddParam {
    private Long sysConfigId;


}
