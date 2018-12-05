package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 关于对应的文件，主要用于删除关于时同步删除文件
 * </p>
 *
 * @author User
 * @since 2018-11-26
 */
public class AboutFile extends BaseEntity<AboutFile> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关于id
     */
    private String aboutId;

    /**
     * 文件id
     */
    private String fileId;

    public String getAboutId() {
        return aboutId;
    }

    public void setAboutId(String aboutId) {
        this.aboutId = aboutId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "AboutFile{" +
                "aboutId=" + aboutId +
                ", fileId=" + fileId +
                "}";
    }
}
