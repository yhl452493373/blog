package com.yang.blog.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 文章、评论的点赞记录表
 * </p>
 *
 * @author User
 * @since 2018-11-23
 */
public class Praise implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 被赞文章id，和评论id二选一，不可同时有值
     */
    private String articleId;

    /**
     * 评论id，和文章id二选一，不可同时有值
     */
    private String commentId;

    /**
     * 点赞人id。如果没登录则没有
     */
    private String userId;

    /**
     * 点赞时间
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
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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
        return "Praise{" +
        "id=" + id +
        ", articleId=" + articleId +
        ", commentId=" + commentId +
        ", userId=" + userId +
        ", createdTime=" + createdTime +
        "}";
    }
}
