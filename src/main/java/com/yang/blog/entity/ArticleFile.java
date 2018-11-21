package com.yang.blog.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 文章对应的文件，主要用于删除文章时同步删除文件
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
public class ArticleFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 文章id
     */
    private String articleId;

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
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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
        return "ArticleFile{" +
        "id=" + id +
        ", articleId=" + articleId +
        ", fileId=" + fileId +
        ", userId=" + userId +
        ", createdTime=" + createdTime +
        "}";
    }
}
