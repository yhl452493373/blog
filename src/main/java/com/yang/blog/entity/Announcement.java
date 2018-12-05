package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 公告信息
 * </p>
 *
 * @author User
 * @since 2018-11-27
 */
public class Announcement extends BaseEntity<Announcement> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告状态：-1-删除，0-不可见，1-正常，2-临时（此状态会被定期清理）
     */
    private Integer available;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "content=" + content +
                ", available=" + available +
                "}";
    }
}
