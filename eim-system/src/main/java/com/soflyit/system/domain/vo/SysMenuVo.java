package com.soflyit.system.domain.vo;

import com.soflyit.system.api.domain.SysMenu;
import lombok.Data;

/**
 * @BelongsProject: eim-module-system
 * @BelongsPackage: com.soflyit.system.domain.vo
 * @Author: JN.G
 * @CreateTime: 2024-01-18  17:52
 * @Version: 1.0
 */
@Data
public class SysMenuVo extends SysMenu {
    public String belongAppName;
}
