package com.soflyit.auth.form;

/**
 * 用户登录对象
 *
 * @author soflyit
 */
public class LoginBody {

    private String username;


    private String password;


    private String wechatCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWechatCode() {
        return wechatCode;
    }

    public void setWechatCode(String wechatCode) {
        this.wechatCode = wechatCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
