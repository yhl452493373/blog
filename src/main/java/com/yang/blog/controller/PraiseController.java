package com.yang.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.bean.JSONResult;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.Article;
import com.yang.blog.entity.Comment;
import com.yang.blog.entity.Message;
import com.yang.blog.entity.Praise;
import com.yang.blog.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author User
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/data/praise")
public class PraiseController {
    private final Logger logger = LoggerFactory.getLogger(PraiseController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 添加数据
     *
     * @param praise 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(Praise praise) {
        JSONResult jsonResult = JSONResult.init();
        if (SecurityUtils.getSubject().isAuthenticated()) {
            praise.setUserId(ShiroUtils.getLoginUser().getId());
        }
        praise.setCreatedTime(LocalDateTime.now());
        JSONObject countJSON = new JSONObject();
        if (StringUtils.isNotEmpty(praise.getArticleId())) {
            Article article = service.articleService.getById(praise.getArticleId());
            article.setPraiseCount(article.getPraiseCount() + 1);
            service.articleService.updateById(article);
            article = service.articleService.getById(article.getId());
            countJSON.put("readCount", article.getReadCount());
            countJSON.put("praiseCount", article.getPraiseCount());
        } else if (StringUtils.isNotEmpty(praise.getCommentId())) {
            Comment comment = service.commentService.getById(praise.getCommentId());
            comment.setPraiseCount(comment.getPraiseCount() + 1);
            service.commentService.updateById(comment);
            comment = service.commentService.getById(comment.getId());
            countJSON.put("praiseCount", comment.getPraiseCount());
        } else if (StringUtils.isNotEmpty(praise.getMessageId())) {
            Message message = service.messageService.getById(praise.getMessageId());
            message.setPraiseCount(message.getPraiseCount() + 1);
            service.messageService.updateById(message);
            message = service.messageService.getById(message.getId());
            countJSON.put("praiseCount", message.getPraiseCount());
        } else {
            return jsonResult.error("点赞对象不存在");
        }
        boolean result = service.praiseService.save(praise);
        if (result)
            jsonResult.success("点赞成功").data(countJSON);
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param praise 删除对象
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public JSONResult delete(Praise praise) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<Praise> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(praise);
        boolean result = service.praiseService.remove(queryWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}