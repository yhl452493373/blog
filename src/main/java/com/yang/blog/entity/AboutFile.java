package com.yang.blog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 关于对应的文件，主要用于删除关于时同步删除文件
 * </p>
 *
 * @author User
 * @since 2018-11-26
 */
public class AboutFile implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 关于id
     */
    private String aboutId;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 关联关系所属用户id
     */
    private String userId;

    /**
     * 关联时的时间
     */
    private LocalDateTime createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAboutId() {
        return aboutId;
    }

    public void setAboutId(String aboutId) {
        this.aboutId = aboutId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    @Override
    public String toString() {
        return "AboutFile{" +
                "id=" + id +
                ", aboutId=" + aboutId +
                ", fileId=" + fileId +
                ", userId=" + userId +
                ", createdTime=" + createdTime +
                "}";
    }
}
