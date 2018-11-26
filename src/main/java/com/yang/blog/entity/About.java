package com.yang.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 关于
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class About implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 关于所属用户id
     */
    private String userId;

    /**
     * 关于的内容
     */
    private String content;

    /**
     * 关于的填写时间
     */
    private LocalDateTime createdTime;

    /**
     * 关于状态。-1-删除，0-不可见，1-可见。此表中每个用户最多有1个是可见状态
     */
    private Integer available;

    /**
     * 与关于对应的文件id，多个用逗号分隔
     */
    @TableField(exist = false)
    private String fileIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    @Override
    public String toString() {
        return "About{" +
                "id=" + id +
                ", userId=" + userId +
                ", content=" + content +
                ", createdTime=" + createdTime +
                ", available=" + available +
                ", fileIds=" + fileIds +
                "}";
    }
}
