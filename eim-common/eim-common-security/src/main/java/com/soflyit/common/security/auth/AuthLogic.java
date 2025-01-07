package com.soflyit.common.security.auth;

import com.soflyit.common.core.exception.auth.NotLoginException;
import com.soflyit.common.core.exception.auth.NotPermissionException;
import com.soflyit.common.core.exception.auth.NotRoleException;
import com.soflyit.common.core.utils.SpringUtils;
import com.soflyit.common.core.utils.StringUtils;
import com.soflyit.common.security.annotation.Logical;
import com.soflyit.common.security.annotation.RequiresLogin;
import com.soflyit.common.security.annotation.RequiresPermissions;
import com.soflyit.common.security.annotation.RequiresRoles;
import com.soflyit.common.security.service.TokenService;
import com.soflyit.common.security.utils.SecurityUtils;
import com.soflyit.system.api.model.LoginUser;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Token 权限验证，逻辑实现类
 *
 * @author soflyit
 */
public class AuthLogic {

    private static final String ALL_PERMISSION = "*:*:*";


    private static final String SUPER_ADMIN = "admin";

    public final TokenService tokenService = SpringUtils.getBean(TokenService.class);


    public void logout() {
        String token = SecurityUtils.getToken();
        if (token == null) {
            return;
        }
        logoutByToken(token);
    }


    public void logoutByToken(String token) {
        tokenService.delLoginUser(token);
    }


    public void checkLogin() {
        getLoginUser();
    }


    public LoginUser getLoginUser() {
        String token = SecurityUtils.getToken();
        if (token == null) {
            throw new NotLoginException("未提供token");
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();


        if (loginUser == null) {
            loginUser = getLoginUser(token);
        }

        if (loginUser == null) {
            throw new NotLoginException("无效的token");
        }
        return loginUser;
    }


    public LoginUser getLoginUser(String token) {
        return tokenService.getLoginUser(token);
    }


    public void verifyLoginUserExpire(LoginUser loginUser) {
        tokenService.verifyToken(loginUser);
    }


    public boolean hasPermi(String permission) {
        return hasPermi(getPermiList(), permission);
    }


    public void checkPermi(String permission) {
        if (!hasPermi(getPermiList(), permission)) {
            throw new NotPermissionException(permission);
        }
    }


    public void checkPermi(RequiresPermissions requiresPermissions) {
        if (requiresPermissions.logical() == Logical.AND) {
            checkPermiAnd(requiresPermissions.value());
        } else {
            checkPermiOr(requiresPermissions.value());
        }
    }


    public void checkPermiAnd(String... permissions) {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions) {
            if (!hasPermi(permissionList, permission)) {
                throw new NotPermissionException(permission);
            }
        }
    }


    public void checkPermiOr(String... permissions) {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions) {
            if (hasPermi(permissionList, permission)) {
                return;
            }
        }
        if (permissions.length > 0) {
            throw new NotPermissionException(permissions);
        }
    }


    public boolean hasRole(String role) {
        return hasRole(getRoleList(), role);
    }


    public void checkRole(String role) {
        if (!hasRole(role)) {
            throw new NotRoleException(role);
        }
    }


    public void checkRole(RequiresRoles requiresRoles) {
        if (requiresRoles.logical() == Logical.AND) {
            checkRoleAnd(requiresRoles.value());
        } else {
            checkRoleOr(requiresRoles.value());
        }
    }


    public void checkRoleAnd(String... roles) {
        Set<String> roleList = getRoleList();
        for (String role : roles) {
            if (!hasRole(roleList, role)) {
                throw new NotRoleException(role);
            }
        }
    }


    public void checkRoleOr(String... roles) {
        Set<String> roleList = getRoleList();
        for (String role : roles) {
            if (hasRole(roleList, role)) {
                return;
            }
        }
        if (roles.length > 0) {
            throw new NotRoleException(roles);
        }
    }


    public void checkByAnnotation(RequiresLogin at) {
        this.checkLogin();
    }


    public void checkByAnnotation(RequiresRoles at) {
        String[] roleArray = at.value();
        if (at.logical() == Logical.AND) {
            this.checkRoleAnd(roleArray);
        } else {
            this.checkRoleOr(roleArray);
        }
    }


    public void checkByAnnotation(RequiresPermissions at) {
        String[] permissionArray = at.value();
        if (at.logical() == Logical.AND) {
            this.checkPermiAnd(permissionArray);
        } else {
            this.checkPermiOr(permissionArray);
        }
    }


    public Set<String> getRoleList() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getRoles();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }


    public Set<String> getPermiList() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getPermissions();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }


    public boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }


    public boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.contains(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}
