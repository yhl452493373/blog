package com.yang.blog.es.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yang.blog.es.doc.base.EsBaseDoc;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;
import java.time.LocalDateTime;


//index-传统数据库的数据库,必须全小写
//type-传统数据库的表,必须全小写
@Mapping(mappingPath = "es-mapping/EsArticleMapping.json")
@Document(indexName = "blog", type = "article")
public class EsArticle extends EsBaseDoc<EsArticle> implements Serializable {
    /**
     * 博文所属用户id
     */
    private String userId;

    /**
     * 博文标题
     */
    private String title;

    /**
     * 博文内容
     */
    private String content;

    /**
     * 是否是草稿。单个用户最多只会有一个草稿。0-否，1-是
     */
    private Integer isDraft;

    /**
     * 阅读次数
     */
    private Integer readCount;

    /**
     * 点赞次数
     */
    private Integer praiseCount;

    /**
     * 发布时间.这三个注解是为了前台序列化java8 LocalDateTime使用的，需要引入jsr310的包才可以使用
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime publishTime;

    /**
     * 修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime modifiedTime;

    /**
     * 博文状态。-1-删除，0-不可见，1-正常
     */
    private Integer available;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Integer isDraft) {
        this.isDraft = isDraft;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }
}
