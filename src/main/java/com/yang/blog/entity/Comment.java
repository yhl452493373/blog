package com.yang.blog.entity;

import java.time.LocalDateTime;
import java.sql.Blob;
import java.io.Serializable;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 评论所属博文id
     */
    private String articleId;

    /**
     * 评论人名字，外部用户使用，和user_id二选一
     */
    private String userName;

    /**
     * 用户id，内部用户使用，和user_name二选一
     */
    private String userId;

    /**
     * 评论内容
     */
    private Blob content;

    /**
     * 点赞次数
     */
    private Integer praiseCount;

    /**
     * 评论时间
     */
    private LocalDateTime createdTime;

    /**
     * 评论状态。-1-删除，0-不可见，1-正常
     */
    private Integer available;

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
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }
    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
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
        return "Comment{" +
        "id=" + id +
        ", articleId=" + articleId +
        ", userName=" + userName +
        ", userId=" + userId +
        ", content=" + content +
        ", praiseCount=" + praiseCount +
        ", createdTime=" + createdTime +
        ", available=" + available +
        "}";
    }
}
