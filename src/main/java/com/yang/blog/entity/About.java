package com.yang.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 关于
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class About extends BaseEntity<About> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关于的内容
     */
    private String content;

    /**
     * 关于状态。-1-删除，0-不可见，1-可见。此表中每个用户最多有1个是可见状态
     */
    private Integer available;

    /**
     * 与关于对应的文件id，多个用逗号分隔
     */
    @TableField(exist = false)
    private String fileIds;

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

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    @Override
    public String toString() {
        return "About{" +
                "content=" + content +
                ", available=" + available +
                ", fileIds=" + fileIds +
                "}";
    }
}
