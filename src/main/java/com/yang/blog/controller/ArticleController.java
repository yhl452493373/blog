package com.yang.blog.controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;

import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/article")
public class ArticleController {
    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 分页查询数据
     *
     * @param page  分页信息
     * @param article 查询对象
     * @return 查询结果
     */
    @RequestMapping("/list")
    public JSONResult list(Article article, Page<Article> page) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        //TODO 根据需要决定是否模糊查询，字段值从article中获取。以下注释部分为模糊查询示例，使用时需要注释或删除queryWrapper.setEntity(article);
        //queryWrapper.like("数据库字段1","字段值");
        //queryWrapper.or();
        //queryWrapper.like("数据库字段2","字段值");
        queryWrapper.setEntity(article);
        service.articleService.page(page, queryWrapper);
        jsonResult.success().data(page.getRecords()).count(page.getTotal());
        return jsonResult;
    }

    /**
     * 添加数据
     *
     * @param article 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(Article article) {
        JSONResult jsonResult = JSONResult.init();
        boolean result = service.articleService.save(article);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 更新数据
     *
     * @param article 更新对象
     * @return 添加结果
     */
    @RequestMapping("/update")
    public JSONResult update(Article article) {
        JSONResult jsonResult = JSONResult.init();
        UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
        //TODO 根据需要设置需要更新的列，字段值从article获取。以下注释部分为指定更新列示例，使用时需要注释或删除updateWrapper.setEntity(article);
        //updateWrapper.set("数据库字段1","字段值");
        //updateWrapper.set("数据库字段2","字段值");
        updateWrapper.eq("表示主键的字段","article中表示主键的值");
        boolean result = service.articleService.update(article, updateWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param article 删除对象
     * @param logical 是否逻辑删除。默认false，使用物理删除
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public JSONResult delete(Article article, @RequestParam(required = false, defaultValue = "false") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (logical) {
            UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
            //TODO 根据需要修改表示逻辑删除的列和值。
            updateWrapper.set("表示逻辑删除的字段","表示逻辑删除的值");
            result = service.articleService.update(article,updateWrapper);
        } else {
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(article);
            result = service.articleService.remove(queryWrapper);
        }
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}