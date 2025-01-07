package com.soflyit.system.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.soflyit.common.core.annotation.Excel;
import com.soflyit.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 应用对象 auth_app
 *
 * @author soflyit
 * @date 2022-05-14
 */
@TableName(value = "sys_auth_app")
@ApiModel
public class SysAuthApp extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @TableId
    @ApiModelProperty("主键")
    private Long id;


    @Excel(name = "应用编码")
    @TableField(value = "client_id")
    @ApiModelProperty("应用编码")
    private String clientId;


    @Excel(name = "应用名称")
    @TableField(value = "name")
    @ApiModelProperty("应用名称")
    private String name;


    @Excel(name = "应用登录地址")
    @TableField(value = "login_url")
    @ApiModelProperty("应用登录地址")
    private String loginUrl;


    @Excel(name = "密钥类型：1-md5;2-rsa")
    @TableField(value = "secret_type")
    @ApiModelProperty("密钥类型：1-md5;2-rsa")
    private Integer secretType;


    @Excel(name = "公钥")
    @TableField(value = "secret_pub")
    @ApiModelProperty("公钥")
    private String secretPub;


    @Excel(name = "私钥")
    @TableField(value = "secret_private")
    @ApiModelProperty("私钥")
    private String secretPrivate;


    @Excel(name = "单点登录类型：1- oauth")
    @TableField(value = "sso_type")
    @ApiModelProperty("单点登录类型：1- oauth")
    private String ssoType;


    @Excel(name = "认证协议：1- oauth2; 2-jwt;")
    @TableField(value = "auth_protocol")
    @ApiModelProperty("认证协议：1- oauth2; 2-jwt;")
    private Integer authProtocol;


    @Excel(name = "授权模式：1-code;2-password")
    @TableField(value = "auth_mode")
    @ApiModelProperty("授权模式：1-code;2-password")
    private Integer authMode;


    @Excel(name = "状态：1-正常；2-停用")
    @TableField(value = "status")
    @ApiModelProperty("状态：1-正常；2-停用")
    private Integer status;


    @Excel(name = "序号")
    @TableField(value = "seq")
    @ApiModelProperty("序号")
    private Long seq;


    @Excel(name = "退出url")
    @TableField(value = "logout_url")
    @ApiModelProperty("退出url")
    private String logoutUrl;


    @Excel(name = "退出类型：1-无；2-前端；3-后端")
    @TableField(value = "logout_type")
    @ApiModelProperty("退出类型：1-无；2-前端；3-后端")
    private Integer logoutType;



    @TableField(value = "revision")
    @ApiModelProperty("乐观锁")
    private String revision;


    @Excel(name = "创建人")
    @TableField(value = "create_by")
    @ApiModelProperty("创建人")
    private String createBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @Excel(name = "更新人")
    @TableField(value = "update_by")
    @ApiModelProperty("更新人")
    private String updateBy;


    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Integer getSecretType() {
        return secretType;
    }

    public void setSecretType(Integer secretType) {
        this.secretType = secretType;
    }

    public String getSecretPub() {
        return secretPub;
    }

    public void setSecretPub(String secretPub) {
        this.secretPub = secretPub;
    }

    public String getSecretPrivate() {
        return secretPrivate;
    }

    public void setSecretPrivate(String secretPrivate) {
        this.secretPrivate = secretPrivate;
    }

    public String getSsoType() {
        return ssoType;
    }

    public void setSsoType(String ssoType) {
        this.ssoType = ssoType;
    }

    public Integer getAuthProtocol() {
        return authProtocol;
    }

    public void setAuthProtocol(Integer authProtocol) {
        this.authProtocol = authProtocol;
    }

    public Integer getAuthMode() {
        return authMode;
    }

    public void setAuthMode(Integer authMode) {
        this.authMode = authMode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public Integer getLogoutType() {
        return logoutType;
    }

    public void setLogoutType(Integer logoutType) {
        this.logoutType = logoutType;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SysAuthApp{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", name='" + name + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                ", secretType=" + secretType +
                ", secretPub='" + secretPub + '\'' +
                ", secretPrivate='" + secretPrivate + '\'' +
                ", ssoType='" + ssoType + '\'' +
                ", authProtocol=" + authProtocol +
                ", authMode=" + authMode +
                ", status=" + status +
                ", seq=" + seq +
                ", logoutUrl='" + logoutUrl + '\'' +
                ", logoutType=" + logoutType +
                ", revision='" + revision + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
