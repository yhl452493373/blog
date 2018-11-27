package com.yang.blog.entity;

import com.yang.blog.entity.base.BaseEntity;

import java.io.Serializable;

/**
 * <p>
 * 上传的文件
 * </p>
 *
 * @author User
 * @since 2018-11-20
 */
public class File extends BaseEntity<File> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 原始名字（不包含后缀名）
     */
    private String originalName;

    /**
     * 服务器上文件名字（不包含后缀名）
     */
    private String saveName;

    /**
     * 扩展名（不包含.）
     */
    private String extensionName;

    /**
     * 文件类型（类似 image/jpeg)
     */
    private String fileType;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 上传用户id
     */
    private String userId;

    /**
     * 文件状态。-1-删除，0-不可见，1-正常，2-临时文件
     */
    private Integer available;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "File{" +
                "originalName=" + originalName +
                ", saveName=" + saveName +
                ", extensionName=" + extensionName +
                ", fileType=" + fileType +
                ", size=" + size +
                ", userId=" + userId +
                ", available=" + available +
                "}";
    }
}
