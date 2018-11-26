package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.*;
import com.yang.blog.shiro.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/article")
public class ArticleController implements BaseController {
    private final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 分页查询数据
     *
     * @param page    分页信息
     * @param article 查询对象
     * @return 查询结果
     */
    @RequestMapping("/list")
    public JSONResult list(Article article, Page<Article> page) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>(article);
        queryWrapper.setEntity(article);
        queryWrapper.orderByDesc("publish_time");
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
    public JSONResult add(Article article, @RequestParam(defaultValue = "false", required = false) Boolean isDraft) {
        JSONResult jsonResult = JSONResult.init();
        User user = ShiroUtils.getLoginUser();
        article.setId(CommonUtils.uuid());
        article.setUserId(user.getId());
        article.setIsDraft(isDraft ? Article.IS_DRAFT_TRUE : Article.IS_DRAFT_FALSE);
        article.setReadCount(0);
        article.setPraiseCount(0);
        article.setCreatedTime(LocalDateTime.now());
        article.setPublishTime(article.getCreatedTime());
        article.setAvailable(Article.TEMP);
        boolean articleResult, tagResult, articleTagResult, articleFileResult;
        articleResult = service.articleService.save(article);
        String fileIds = article.getFileIds();
        String tags = article.getTags();
        List<Tag> tagList = null;
        if (StringUtils.isNotEmpty(fileIds)) {
            List<String> fileIdList = CommonUtils.splitIds(fileIds);
            articleFileResult = service.fileService.setAvailable(fileIdList, File.AVAILABLE);

            if (articleFileResult) {
                //将文件和文章关联
                List<ArticleFile> articleFileList = new ArrayList<>();
                fileIdList.forEach(fileId -> {
                    ArticleFile articleFile = new ArticleFile();
                    articleFile.setArticleId(article.getId());
                    articleFile.setFileId(fileId);
                    articleFile.setCreatedTime(LocalDateTime.now());
                    articleFile.setUserId(user.getId());
                    articleFileList.add(articleFile);
                });
                articleFileResult = service.articleFileService.saveBatch(articleFileList);
            }
        } else {
            articleFileResult = true;
        }
        if (StringUtils.isNotEmpty(tags)) {
            List<String> tagNameList = Arrays.asList(tags.split(","));
            QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
            tagQueryWrapper.in("name", tagNameList);
            tagList = service.tagService.list(tagQueryWrapper);
            List<Tag> finalTagList = tagList;
            tagNameList.forEach(tagName -> {
                boolean exist = false;
                for (Tag tag : finalTagList) {
                    if (tag.getName().equals(tagName)) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    tag.setAvailable(Tag.BLOCK);
                    tag.setUserId(user.getId());
                    tag.setCreatedTime(LocalDateTime.now());
                    finalTagList.add(tag);
                }
            });
            tagResult = service.tagService.saveOrUpdateBatch(tagList);
            List<ArticleTag> articleTagList = new ArrayList<>();
            tagList.forEach(tag -> {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTag.setUserId(user.getId());
                articleTag.setCreatedTime(LocalDateTime.now());
                articleTagList.add(articleTag);
            });
            articleTagResult = service.articleTagService.saveBatch(articleTagList);
        } else {
            tagResult = true;
            articleTagResult = true;
        }
        if (articleResult && tagResult && articleTagResult && articleFileResult) {
            article.setAvailable(Article.AVAILABLE);
            service.articleService.updateById(article);
            if (tagList != null) {
                tagList.forEach(tag -> tag.setAvailable(Tag.AVAILABLE));
                service.tagService.updateBatchById(tagList);
            }
            //添加博客成功,返回其id作为data的值,通过id跳转
            jsonResult.success(ADD_SUCCESS).data(article.getId());
        } else {
            jsonResult.error(ADD_FAILED);
        }
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
        updateWrapper.eq("表示主键的字段", "article中表示主键的值");
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
            updateWrapper.set("表示逻辑删除的字段", "表示逻辑删除的值");
            result = service.articleService.update(article, updateWrapper);
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