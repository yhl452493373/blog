package com.yang.blog.config;

import com.github.yhl452493373.shiro.ShiroCaptcha;
import com.yang.blog.es.service.EsArticleService;
import com.yang.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.annotation.PostConstruct;

/**
 * 这个配置文件用于配置所有需要注入的bean,其余地方通过ServiceConfig.serviceConfig.xxxxService方式调用
 * 降低因同一个service在多个文件中分别注入引起的时间消耗
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Configuration
public class ServiceConfig {
    public static ServiceConfig serviceConfig;

    @Autowired
    public ShiroCaptcha shiroCaptcha;
    @Autowired
    public AboutService aboutService;
    @Autowired
    public ArticleService articleService;
    @Autowired
    public CommentService commentService;
    @Autowired
    public FileService fileService;
    @Autowired
    public MessageService messageService;
    @Autowired
    public TagService tagService;
    @Autowired
    public UserService userService;
    @Autowired
    public ArticleTagService articleTagService;
    @Autowired
    public ArticleFileService articleFileService;
    @Autowired
    public PraiseService praiseService;
    @Autowired
    public AboutFileService aboutFileService;
    @Autowired
    public AnnouncementService announcementService;
    @Autowired
    public EsArticleService esArticleService;

    @PostConstruct
    public void init() {
        serviceConfig = this;
    }

    //以下：从配置文件读取一些配置

    @Value("${spring.servlet.multipart.max-file-size:1MB}")
    public DataSize maxFileSize;

    @Value("${max-request-size:10MB}")
    public DataSize maxRequestSize;

    public DataSize getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(DataSize maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public DataSize getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(DataSize maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }
}
