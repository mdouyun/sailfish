package org.thorn.sailfish.entity;

import org.thorn.sailfish.enums.YesOrNoEnum;

import java.util.Date;

/**
 * @Author: yfchenyun
 * @Since: 2014-3-13 16:03:33
 * @Version: 1.0
 */
public class User {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 默认角色
     */
    private String defaultRole;

    /**
     * 是否可用,0可用,1不可用
     */
    private Integer available;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 上次登录时间
     */
    private Date lastLoginTime;


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
         return this.userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
         return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
         return this.password;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public String getDefaultRole() {
         return this.defaultRole;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getAvailable() {
         return this.available;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() {
         return this.modifyTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() {
         return this.lastLoginTime;
    }

    public String getAvailableStatus() {
        return YesOrNoEnum.getNameByCode(available);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", defaultRole='").append(defaultRole).append('\'');
        sb.append(", available=").append(available);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append('}');
        return sb.toString();
    }
}