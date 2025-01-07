package com.soflyit.system.api.model;

import com.soflyit.system.api.domain.SysUser;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息
 *
 * @author soflyit
 */
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;


    private String token;


    private Long userid;


    private String username;


    private String tenantCode;


    private Long loginTime;


    private Long expireTime;


    private String ipaddr;


    private Set<String> permissions;


    private Set<String> roles;


    private SysUser sysUser;


    private Boolean kickoutFlag;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Boolean getKickoutFlag() {
        return kickoutFlag;
    }

    public void setKickoutFlag(Boolean kickoutFlag) {
        this.kickoutFlag = kickoutFlag;
    }
}
