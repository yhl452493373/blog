package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 留言
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class Message extends BaseEntity<Message> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 留言人名字，外部用户使用，和user_id二选一
     */
    private String userName;

    /**
     * 留言人id，内部用户使用。和user_name二选一
     */
    private String userId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 点赞次数
     */
    private Integer praiseCount;

    /**
     * 留言状态。-1-删除，0-不可见，1-正常
     */
    private Integer available;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userName=" + userName +
                ", userId=" + userId +
                ", content=" + content +
                ", praiseCount=" + praiseCount +
                ", available=" + available +
                "}";
    }
}
