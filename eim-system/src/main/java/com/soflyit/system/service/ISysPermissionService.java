package com.soflyit.system.service;

import java.util.Set;

public interface ISysPermissionService {

    Set<String> getRolePermission(Long appId, Long userId, Long deptId);


    Set<String> getMenuPermission(Long appId, Long userId, Long deptId);
}
