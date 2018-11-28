package com.yang.blog.entity.base;

import java.time.LocalDateTime;

public abstract class BaseEntity<Entity> implements Base<Entity> {
    /**
     * 记录的id
     */
    private String id;

    /**
     * 记录创建时间
     */
    private LocalDateTime createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
