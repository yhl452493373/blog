package com.yang.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 留言楼层
     */
    private Integer floor;

    /**
     * 留言回复所属的留言id
     */
    private String belongId;

    /**
     * 留言回复在所属留言中的楼层
     */
    private Integer belongFloor;

    /**
     * 留言回复列表
     */
    @TableField(exist = false)
    private List<Message> replyList;

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

    public List<Message> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Message> replyList) {
        this.replyList = replyList;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userName=" + userName +
                ", userId=" + userId +
                ", content=" + content +
                ", praiseCount=" + praiseCount +
                ", available=" + available +
                ", floor=" + floor +
                ", belongId=" + belongId +
                ", belongFloor=" + belongFloor +
                "}";
    }
}
