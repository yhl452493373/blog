package com.yang.blog.config;

import com.github.yhl452493373.shiro.ShiroCaptcha;
import com.yang.blog.service.AboutService;
import com.yang.blog.service.ArticleService;
import com.yang.blog.service.CommentService;
import com.yang.blog.service.FileService;
import com.yang.blog.service.MessageService;
import com.yang.blog.service.TagService;
import com.yang.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 这个配置文件用于配置所有需要注入的bean,其余地方通过ServiceConfig.serviceConfig.xxxxService方式调用
 * 降低因同一个service在多个文件中分别注入引起的时间消耗
 */
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

    @PostConstruct
    public void init() {
        serviceConfig = this;
    }
}