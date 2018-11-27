package com.yang.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class User extends BaseEntity<User> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 密码加密的盐
     */
    private String salt;

    /**
     * 加密次数
     */
    private Integer hashCount;

    /**
     * 头像文件id
     */
    private String avatarFileId;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

    /**
     * 用户是否可用。-1-删除，0-锁定，1-正常
     */
    private Integer available;

    /**
     * 注册时输入的确认密码
     */
    @TableField(exist = false)
    private String confirmPassword;

    /**
     * 用户输入的验证码
     */
    @TableField(exist = false)
    private String captcha;

    /**
     * 是否记住我
     */
    @TableField(exist = false)
    private String isRememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getHashCount() {
        return hashCount;
    }

    public void setHashCount(Integer hashCount) {
        this.hashCount = hashCount;
    }

    public String getAvatarFileId() {
        return avatarFileId;
    }

    public void setAvatarFileId(String avatarFileId) {
        this.avatarFileId = avatarFileId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getIsRememberMe() {
        return isRememberMe;
    }

    public void setIsRememberMe(String isRememberMe) {
        this.isRememberMe = isRememberMe;
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                ", password=" + password +
                ", salt=" + salt +
                ", hashCount=" + hashCount +
                ", avatarFileId=" + avatarFileId +
                ", phoneNumber=" + phoneNumber +
                ", email=" + email +
                ", modifiedTime=" + modifiedTime +
                ", available=" + available +
                ", confirmPassword=" + confirmPassword +
                ", captcha=" + captcha +
                ", isRememberMe=" + isRememberMe +
                "}";
    }
}
