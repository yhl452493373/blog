package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 博文与标签的对应关系
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
public class ArticleTag extends BaseEntity<ArticleTag> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return "ArticleTag{" +
                "articleId=" + articleId +
                ", tagId=" + tagId +
                ", userId=" + userId +
                "}";
    }
}
