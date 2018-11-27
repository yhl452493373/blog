package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 文章、评论的点赞记录表
 * </p>
 *
 * @author User
 * @since 2018-11-23
 */
public class Praise extends BaseEntity<Praise> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 被赞文章id，和评论id，留言id，三选一，不可同时有值
     */
    private String articleId;

    /**
     * 评论id，和文章id，留言id，三选一，不可同时有值
     */
    private String commentId;

    /**
     * 留言id，和文章id，评论id，三选一，不可同时有值
     */
    private String messageId;

    /**
     * 点赞人id。如果没登录则没有
     */
    private String userId;

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Praise{" +
                "articleId=" + articleId +
                ", commentId=" + commentId +
                ", messageId=" + messageId +
                ", userId=" + userId +
                "}";
    }
}
