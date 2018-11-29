package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.*;
import com.yang.blog.es.doc.EsArticle;
import com.yang.blog.shiro.ShiroUtils;
import com.yang.blog.util.FileUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
        jsonResult.success(QUERY_SUCCESS).data(page.getRecords()).count(page.getTotal());
        return jsonResult;
    }

    /**
     * 添加数据
     *
     * @param article 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    @SuppressWarnings("Duplicates")
    public JSONResult add(Article article, @RequestParam(defaultValue = "false", required = false) Boolean isDraft) {
        JSONResult jsonResult = JSONResult.init();
        if (StringUtils.isEmpty(article.getTitle()))
            return jsonResult.error(ADD_FAILED + "标题不能为空");
        if (StringUtils.isEmpty(article.getContent()))
            return jsonResult.error(ADD_FAILED + "内容不能为空");
        User user = ShiroUtils.getLoginUser();
        article.setId(CommonUtils.uuid());
        article.setUserId(user.getId());
        article.setIsDraft(isDraft ? Article.IS_DRAFT_TRUE : Article.IS_DRAFT_FALSE);
        article.setReadCount(0);
        article.setPraiseCount(0);
        article.setCreatedTime(LocalDateTime.now());
        article.setPublishTime(article.getCreatedTime());
        article.setAvailable(Article.TEMP);
        boolean articleResult, articleTagResult, articleFileResult;
        articleResult = service.articleService.save(article);
        String fileIds = article.getFileIds();
        String tags = article.getTags();
        if (StringUtils.isNotEmpty(fileIds)) {
            articleFileResult = relateArticleAndFile(article);
        } else {
            articleFileResult = true;
        }
        if (StringUtils.isNotEmpty(tags)) {
            articleTagResult = relateArticleAndTag(article);
        } else {
            articleTagResult = true;
        }
        if (articleResult && articleTagResult && articleFileResult) {
            article.setAvailable(Article.AVAILABLE);
            service.articleService.updateById(article);
            //同步写入到es数据库
            service.esArticleService.save(new EsArticle().update(true, article));
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
    @SuppressWarnings("Duplicates")
    @RequestMapping("/update")
    public JSONResult update(Article article) {
        JSONResult jsonResult = JSONResult.init();
        if (StringUtils.isEmpty(article.getId()))
            return jsonResult.error(UPDATE_FAILED + "id参数异常");
        if (StringUtils.isEmpty(article.getTitle()))
            return jsonResult.error(UPDATE_FAILED + "标题不能为空");
        if (StringUtils.isEmpty(article.getContent()))
            return jsonResult.error(UPDATE_FAILED + "内容不能为空");
        article.setModifiedTime(LocalDateTime.now());
        Article old = service.articleService.getById(article.getId());
        if (old == null)
            return jsonResult.error(UPDATE_FAILED + "记录id不正确");
        old.update(true, article);
        boolean articleResult = service.articleService.updateById(old),
                articleFileResult = relateArticleAndFile(old),
                articleTagResult = relateArticleAndTag(old);
        //将数据同步更新到es中
        service.esArticleService.save(new EsArticle().update(true, old));
        if (articleResult && articleFileResult && articleTagResult) {
            jsonResult.success(UPDATE_SUCCESS).data(article.getId());
        } else {
            jsonResult.error(UPDATE_FAILED);
        }
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

    @RequestMapping("/search")
    public JSONResult search(String content, Boolean searchTitle, Boolean searchContent, Page<EsArticle> page) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (searchTitle)
            queryBuilder.should(QueryBuilders.matchQuery("title", content));
        if (searchContent)
            queryBuilder.should(QueryBuilders.matchQuery("content", content));
        service.esArticleService.search(page, queryBuilder);
        return JSONResult.init().success(QUERY_SUCCESS).data(page.getRecords()).count(page.getTotal());
    }

    /**
     * 将指定id的文件和article对象关联起来
     *
     * @param article 实体对象
     * @return 关联结果
     */
    @SuppressWarnings("Duplicates")
    private boolean relateArticleAndFile(Article article) {
        String fileIds = article.getFileIds();
        boolean fileResult = true, articleFileResult = true;
        List<String> fileIdList = CommonUtils.splitIds(fileIds);
        if (!fileIdList.isEmpty()) {
            User user = ShiroUtils.getLoginUser();
            fileResult = service.fileService.setAvailable(fileIdList, File.AVAILABLE);
            if (fileResult) {
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
        }

        if (fileResult) {
            //清理临时状态的文件.此一步操作稍作修改可作为定时任务一部分
            QueryWrapper<ArticleFile> aboutQueryWrapper = new QueryWrapper<>();
            aboutQueryWrapper.eq("article_id", article.getId());
            List<ArticleFile> aboutFileList = service.articleFileService.list(aboutQueryWrapper);
            List<String> aboutFileIdList = CommonUtils.convertToFieldList(aboutFileList, "getFileId");
            QueryWrapper<File> fileQueryWrapper = new QueryWrapper<>();
            fileQueryWrapper.in("id", aboutFileIdList);
            fileQueryWrapper.eq("available", File.TEMP);
            List<File> tempFileList = service.fileService.list(fileQueryWrapper);
            articleFileResult = FileUtils.delete(CommonUtils.convertToIdList(tempFileList));
        }
        return fileResult && articleFileResult;
    }

    /**
     * 将指定id的标签和article对象关联起来
     *
     * @param article 实体对象
     * @return 关联结果
     */
    @SuppressWarnings("Duplicates")
    private boolean relateArticleAndTag(Article article) {
        boolean result;
        User user = ShiroUtils.getLoginUser();
        String tags = article.getTags();
        List<String> tagNameList = new ArrayList<>(Arrays.asList(tags.split(",")));
        //最后与文章关联的标签
        List<String> relateTagName = new ArrayList<>(tagNameList);
        //删除""导致的一个无意义标签
        relateTagName.remove("");
        tagNameList = new ArrayList<>(relateTagName);

        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("user_id", user.getId());
        articleTagQueryWrapper.eq("article_id", article.getId());
        result = service.articleTagService.remove(articleTagQueryWrapper);
        if (!result)
            return false;
        if (tagNameList.isEmpty())
            return true;
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("user_id", user.getId());
        tagQueryWrapper.in("name", tagNameList);
        //拥有的所有的tag
        List<Tag> tagList = service.tagService.list();
        for (Tag tag : tagList) {
            //剔除已保存的tag
            tagNameList.remove(tag.getName());
        }
        List<Tag> finalTagList = tagList;
        tagNameList.forEach(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            tag.setUserId(user.getId());
            tag.setAvailable(Tag.AVAILABLE);
            tag.setCreatedTime(LocalDateTime.now());
            finalTagList.add(tag);
        });
        List<Tag> addTagList = new ArrayList<>();
        tagList.forEach(tag -> {
            //没有id,表示新增
            if (StringUtils.isEmpty(tag.getId())) {
                addTagList.add(tag);
            }
        });
        //保存新增标签
        if (!addTagList.isEmpty()) {
            result = service.tagService.saveBatch(addTagList);
            if (!result)
                return false;
        }
        //所有新增标签保存后,查询问斩关联的对应标签记录
        tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("user_id", user.getId());
        tagQueryWrapper.in("name", relateTagName);
        tagList = service.tagService.list(tagQueryWrapper);
        List<ArticleTag> articleTagList = new ArrayList<>();
        //标签与文章关联
        tagList.forEach(tag -> {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setUserId(user.getId());
            articleTag.setArticleId(article.getId());
            articleTag.setCreatedTime(LocalDateTime.now());
            articleTag.setTagId(tag.getId());
            articleTagList.add(articleTag);
        });
        return service.articleTagService.saveBatch(articleTagList);
    }
}