package com.yang.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 文章标签
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class Tag extends BaseEntity<Tag> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签名字
     */
    private String name;

    /**
     * 标签是否可用。-1-删除，0-禁用，1-正常
     */
    private Integer available;

    /**
     * 标签下的文章
     */
    @TableField(exist = false)
    private List<Article> articleList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return "Tag{" +
                " name=" + name +
                ", available=" + available +
                "}";
    }
}
