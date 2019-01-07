package com.yang.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class Comment extends BaseEntity<Comment> implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String content;

    /**
     * 点赞次数
     */
    private Integer praiseCount;

    /**
     * 评论状态。-1-删除，0-不可见，1-正常
     */
    private Integer available;

    /**
     * 评论楼层
     */
    private Integer floor;

    /**
     * 评论所属的评论id
     */
    private String belongId;

    /**
     * 评论在所属评论中的楼层
     */
    private Integer belongFloor;

    /**
     * 回复列表
     */
    @TableField(exist = false)
    private List<Comment> replyList;

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

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public Integer getBelongFloor() {
        return belongFloor;
    }

    public void setBelongFloor(Integer belongFloor) {
        this.belongFloor = belongFloor;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "articleId=" + articleId +
                ", userName=" + userName +
                ", userId=" + userId +
                ", content=" + content +
                ", praiseCount=" + praiseCount +
                ", available=" + available +
                ", floor=" + floor +
                ", belongId=" + belongId +
                ", belongFloor=" + belongFloor +
                "}";
    }

    public List<Comment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Comment> replyList) {
        this.replyList = replyList;
    }
}
