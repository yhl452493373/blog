package com.yang.blog.bean;

import org.springframework.web.multipart.MultipartFile;

/**
 * 大文件分片入参实体
 */
public class MultipartFileParam {
    /**
     * 文件传输任务ID
     */
    private String taskId;

    /**
     * 当前为第几分片
     */
    private int chunk;

    /**
     * 每个分块的大小
     */
    private long size;

    /**
     * 分片总数
     */
    private int chunkTotal;

    /**
     * 分块文件传输对象
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getChunkTotal() {
        return chunkTotal;
    }

    public void setChunkTotal(int chunkTotal) {
        this.chunkTotal = chunkTotal;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
