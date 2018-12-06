package com.yang.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.yhl452493373.bean.JSONResult;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.Article;
import com.yang.blog.entity.Comment;
import com.yang.blog.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param praiseType 点赞对象类型
     * @param praiseId   点赞对象Id
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(String praiseType, String praiseId) {
        JSONResult jsonResult = JSONResult.init();
        JSONObject countJSON = new JSONObject();
        if (praiseType.equalsIgnoreCase("article")) {
            Article article = service.articleService.getById(praiseId);
            article.setPraiseCount(article.getPraiseCount() + 1);
            service.articleService.updateById(article);
            article = service.articleService.getById(article.getId());
            countJSON.put("readCount", article.getReadCount());
            countJSON.put("praiseCount", article.getPraiseCount());
        } else if (praiseType.equalsIgnoreCase("comment")) {
            Comment comment = service.commentService.getById(praiseId);
            comment.setPraiseCount(comment.getPraiseCount() + 1);
            service.commentService.updateById(comment);
            comment = service.commentService.getById(comment.getId());
            countJSON.put("praiseCount", comment.getPraiseCount());
        } else if (praiseType.equalsIgnoreCase("message")) {
            Message message = service.messageService.getById(praiseId);
            message.setPraiseCount(message.getPraiseCount() + 1);
            service.messageService.updateById(message);
            message = service.messageService.getById(message.getId());
            countJSON.put("praiseCount", message.getPraiseCount());
        } else {
            return jsonResult.error("点赞对象不存在");
        }
        return jsonResult.success("点赞成功").data(countJSON);
    }
}