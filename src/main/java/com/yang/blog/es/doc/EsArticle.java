package com.yang.blog.es.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yang.blog.annotation.FieldNotUpdate;
import com.yang.blog.entity.Article;
import com.yang.blog.entity.base.Constant;
import com.yang.blog.es.doc.base.EsBaseDoc;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;


//index-传统数据库的数据库,必须全小写
//type-传统数据库的表,必须全小写
@Document(indexName = "article", type = "doc")
public class EsArticle extends EsBaseDoc<EsArticle, Article> implements Constant, Serializable {
    /**
     * article对应的type,用于过滤.注意,如果有和这个字段重名的,需要单独取名字.如果用到了logstash同步mysql数据到es,此处名字则需要和logstash中的type一致
     */
    @FieldNotUpdate
    @Field(type = FieldType.Keyword)
    private String type = DOC_TYPE;

    /**
     * 博文标题
     */
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "hanlp-smart", analyzer = "hanlp-index")
    private String title;

    /**
     * 博文内容
     */
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "hanlp-smart", analyzer = "hanlp-index")
    private String content;

    /**
     * 博文摘要
     */
    @Field(type = FieldType.Text, fielddata = true, searchAnalyzer = "hanlp-smart", analyzer = "hanlp-index")
    private String summary;

    /**
     * 是否是草稿。单个用户最多只会有一个草稿。0-否，1-是
     */
    @Field(type = FieldType.Integer)
    private Integer isDraft;

    /**
     * 阅读次数
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer readCount;

    /**
     * 点赞次数
     */
    @Field(index = false, type = FieldType.Integer)
    private Integer praiseCount;

    /**
     * 发布时间.这三个注解是为了前台序列化java8 LocalDateTime使用的，需要引入jsr310的包才可以使用
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Keyword)
    private LocalDateTime publishTime;

    /**
     * 修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Keyword)
    private LocalDateTime modifiedTime;

    /**
     * 博文状态。-1-删除，0-不可见，1-正常
     */
    @Field(type = FieldType.Integer)
    private Integer available;

    public String getType() {
        return type;
    }

    /**
     * 此字段用于es的文档分类,更新时不需要更新
     */
    public void setType(String type) {
        this.type = type;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
