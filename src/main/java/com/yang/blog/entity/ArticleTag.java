package com.yang.blog.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 博文与标签的对应关系
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
public class ArticleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 博文id
     */
    private String articleId;

    /**
     * 标签id
     */
    private String tagId;

    /**
     * 文章-标签对应关系所属用户
     */
    private String userId;

    /**
     * 关联时间
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

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
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
        return "ArticleTag{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", tagId=" + tagId +
                ", userId=" + userId +
                ", createdTime=" + createdTime +
                "}";
    }
}
