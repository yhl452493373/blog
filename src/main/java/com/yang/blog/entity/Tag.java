package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 文章标签
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class Tag extends BaseEntity<Tag> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名字
     */
    private String name;

    /**
     * 标签所属用户
     */
    private String userId;

    /**
     * 标签是否可用。-1-删除，0-禁用，1-正常
     */
    private Integer available;

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

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Tag{" +
                " name=" + name +
                ", userId=" + userId +
                ", available=" + available +
                "}";
    }
}
