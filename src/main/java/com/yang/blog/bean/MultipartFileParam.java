package com.yang.blog.bean;

import org.springframework.web.multipart.MultipartFile;

/**
 * 大文件分片入参实体
 */
public class MultipartFileParam {
    /**
     * 文件传输任务ID,由于用的uuid,因此可以用于临时文件名
     */
    private String taskId;

    /**
     * 当前为第几分片
     */
    private int chunk;

    /**
     * 每个分片的大小
     */
    private long chunkSize;

    /**
     * 分片总数
     */
    private int chunkTotal;

    /**
     * 上传文件的文件名
     */
    private String fileName;

    /**
     * 上传文件的文件类型
     */
    private String fileType;

    /**
     * 上传文件的文件大小
     */
    private long fileSize;

    /**
     * 分片文件传输对象.如果不分片则为整个文件对象
     */
    private MultipartFile file;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getChunkTotal() {
        return chunkTotal;
    }

    public void setChunkTotal(int chunkTotal) {
        this.chunkTotal = chunkTotal;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
