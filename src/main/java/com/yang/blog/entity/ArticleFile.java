package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 文章对应的文件，主要用于删除文章时同步删除文件
 * </p>
 *
 * @author User
 * @since 2018-11-21
 */
public class ArticleFile extends BaseEntity<ArticleFile> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 文件id
     */
    private String fileId;

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

    @Override
    public String toString() {
        return "ArticleFile{" +
                "articleId=" + articleId +
                ", fileId=" + fileId +
                "}";
    }
}
