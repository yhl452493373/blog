package com.yang.blog.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 文章标签
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 标签名字
     */
    private String name;

    /**
     * 标签所属用户
     */
    private String userId;

    /**
     * 标签创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 标签是否可用。-1-删除，0-禁用，1-正常
     */
    private Integer available;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Tag{" +
        "id=" + id +
        ", name=" + name +
        ", userId=" + userId +
        ", createdTime=" + createdTime +
        ", available=" + available +
        "}";
    }
}
