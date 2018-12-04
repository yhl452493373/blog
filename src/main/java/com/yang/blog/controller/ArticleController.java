package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.hankcs.hanlp.HanLP;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.*;
import com.yang.blog.es.doc.EsArticle;
import com.yang.blog.shiro.ShiroUtils;
import com.yang.blog.util.FileUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

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
        queryWrapper.eq("available", Article.AVAILABLE);
        queryWrapper.orderByDesc("publish_time");
        service.articleService.page(page, queryWrapper);
        jsonResult.success(QUERY_SUCCESS)
                .data(page.getRecords(), JSONResult.Pattern.INCLUDE, "id", "title", "summary", "publishTime")
                .count(page.getTotal());
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
        article.setSummary(HanLP.getSummary(article.getContent(), 255));
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
            service.esArticleService.save(new EsArticle().update(true, article, EsArticle.class));
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
        article.setSummary(HanLP.getSummary(article.getContent(), 255));
        Article old = service.articleService.getById(article.getId());
        if (old == null)
            return jsonResult.error(UPDATE_FAILED + "记录id不正确");
        old.update(true, article);
        boolean articleResult = service.articleService.updateById(old),
                articleFileResult = relateArticleAndFile(old),
                articleTagResult = relateArticleAndTag(old);
        //将数据同步更新到es中
        service.esArticleService.save(new EsArticle().update(true, old, EsArticle.class));
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
    public JSONResult delete(Article article, @RequestParam(required = false, defaultValue = "true") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (logical) {
            UpdateWrapper<Article> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("available", Article.DELETE);
            updateWrapper.eq("id", article.getId());
            result = service.articleService.update(article, updateWrapper);
            Article old = service.articleService.getById(article.getId());
            old.setAvailable(Article.DELETE);
            service.esArticleService.save(new EsArticle().update(true, old, EsArticle.class));
        } else {
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(article);
            result = service.articleService.remove(queryWrapper);
        }
        if (result)
            jsonResult.success(DELETE_SUCCESS);
        else
            jsonResult.error(DELETE_FAILED);
        return jsonResult;
    }

    @RequestMapping("/search")
    public JSONResult search(String content, @RequestParam(defaultValue = "false") Boolean includeContent, Page<EsArticle> page) {
        if (StringUtils.isEmpty(content) || content.trim().length() < 2)
            return JSONResult.init().error(QUERY_FAILED + "搜索关键词过短");
        //过滤
        BoolQueryBuilder filterBuilder = QueryBuilders.boolQuery();
        filterBuilder.filter(QueryBuilders.termQuery("isDraft", Article.IS_DRAFT_FALSE));
        filterBuilder.filter(QueryBuilders.termQuery("type", EsArticle.DOC_TYPE));
        filterBuilder.filter(QueryBuilders.termQuery("available", EsArticle.AVAILABLE));
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //从title或者content中搜索
        //由于article是从基础数据库同步的数据,所以数据是一样的
        queryBuilder.should(QueryBuilders.matchQuery("title", content));
        if (includeContent)
            queryBuilder.should(QueryBuilders.matchQuery("content", content));
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(filterBuilder).withQuery(queryBuilder).build();
        //排序
        page.setDesc("publishTime");
        service.esArticleService.search(page, searchQuery);
        return JSONResult.init().success(QUERY_SUCCESS)
                .data(page.getRecords(), JSONResult.Pattern.INCLUDE, "id", "title", "summary", "publishTime", "readCount", "praiseCount")
                .count(page.getTotal());
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
        User user = ShiroUtils.getLoginUser();
        //与当前文章相关文件关系表的查询条件
        QueryWrapper<ArticleFile> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("user_id", user.getId());
        articleQueryWrapper.eq("article_id", article.getId());
        //传入的所有文件id
        List<String> fileIdList = CommonUtils.splitIds(fileIds);
        if (!fileIdList.isEmpty()) {
            //数据库已有的对应关系
            List<ArticleFile> oldArticleFileList = service.articleFileService.list(articleQueryWrapper);
            List<String> oldFileIdList = CommonUtils.convertToFieldList(oldArticleFileList, "getFileId");
            Set<String> addFileIdSet = new HashSet<>();
            Set<String> deleteFileIdSet = new HashSet<>();
            //获取需要删除的文件:旧文件id不存在于新的id集合中
            oldFileIdList.forEach(oldFileId -> {
                if (fileIdList.indexOf(oldFileId) == -1) {
                    deleteFileIdSet.add(oldFileId);
                }
            });
            //获取新增的文件
            fileIdList.forEach(fileId -> {
                if (oldFileIdList.indexOf(fileId) == -1)
                    addFileIdSet.add(fileId);
            });
            //删除旧文件
            if (!deleteFileIdSet.isEmpty()) {
                articleQueryWrapper.in("file_id", deleteFileIdSet);
                articleFileResult = service.articleFileService.remove(articleQueryWrapper);
                FileUtils.delete(new ArrayList<>(deleteFileIdSet));
                fileResult = service.fileService.removeByIds(deleteFileIdSet);
            }
            if (articleFileResult) {
                //保存新文件
                List<ArticleFile> newArticleFileList = new ArrayList<>();
                addFileIdSet.forEach(fileId -> {
                    ArticleFile articleFile = new ArticleFile();
                    articleFile.setArticleId(article.getId());
                    articleFile.setFileId(fileId);
                    articleFile.setUserId(user.getId());
                    articleFile.setCreatedTime(LocalDateTime.now());
                    newArticleFileList.add(articleFile);
                });
                articleFileResult = service.articleFileService.saveBatch(newArticleFileList);
                if (articleFileResult) {
                    //将文件状态改为正常
                    QueryWrapper<ArticleFile> articleFileQueryWrapper = new QueryWrapper<>();
                    articleFileQueryWrapper.eq("user_id", user.getId());
                    articleFileQueryWrapper.eq("article_id", article.getId());
                    List<ArticleFile> savedFileList = service.articleFileService.list(articleFileQueryWrapper);
                    List<String> savedFileIdList = CommonUtils.convertToFieldList(savedFileList, "getFileId");
                    Collection<File> fileList = service.fileService.listByIds(savedFileIdList);
                    fileList.forEach(file -> file.setAvailable(File.AVAILABLE));
                    fileResult = service.fileService.updateBatchById(fileList);
                }
            }
        } else {
            //如果新传入文件id为空,则删除所有对应文件
            QueryWrapper<ArticleFile> articleFileQueryWrapper = new QueryWrapper<>();
            articleFileQueryWrapper.eq("user_id", user.getId());
            articleFileQueryWrapper.eq("article_id", article.getId());
            List<ArticleFile> savedFileList = service.articleFileService.list(articleFileQueryWrapper);
            if (!savedFileList.isEmpty()) {
                List<String> savedFileIdList = CommonUtils.convertToFieldList(savedFileList, "getFileId");
                FileUtils.delete(savedFileIdList);
                fileResult = service.fileService.removeByIds(savedFileIdList);
                //删除对应
                articleFileResult = service.articleFileService.remove(articleQueryWrapper);
            }
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

        if (tagNameList.isEmpty()) {
            QueryWrapper<ArticleTag> removeQueryWrapper = new QueryWrapper<>();
            removeQueryWrapper.eq("user_id", user.getId());
            removeQueryWrapper.eq("article_id", article.getId());
            service.articleTagService.remove(removeQueryWrapper);
        }

        QueryWrapper<Tag> oldTagQueryWrapper = new QueryWrapper<>();
        oldTagQueryWrapper.eq("user_id", user.getId());
        oldTagQueryWrapper.in("name", tagNameList);
        List<Tag> oldTagList = service.tagService.list(oldTagQueryWrapper);

        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("user_id", user.getId());
        articleTagQueryWrapper.eq("article_id", article.getId());
        articleTagQueryWrapper.notIn("tag_id", CommonUtils.convertToIdList(oldTagList));

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
        //所有新增标签保存后,查询文章关联的对应标签记录
        tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("user_id", user.getId());
        tagQueryWrapper.in("name", relateTagName);
        tagList = service.tagService.list(tagQueryWrapper);
        articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("user_id", user.getId());
        articleTagQueryWrapper.eq("article_id", article.getId());
        List<ArticleTag> articleTagList = service.articleTagService.list(articleTagQueryWrapper);
        List<String> relatedTagIdList = CommonUtils.convertToFieldList(articleTagList, "getTagId");
        List<ArticleTag> newArticleTagList = new ArrayList<>();
        //标签与文章关联
        tagList.forEach(tag -> {
            if (relatedTagIdList.indexOf(tag.getId()) == -1) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setUserId(user.getId());
                articleTag.setArticleId(article.getId());
                articleTag.setCreatedTime(LocalDateTime.now());
                articleTag.setTagId(tag.getId());
                newArticleTagList.add(articleTag);
            }
        });
        return service.articleTagService.saveBatch(newArticleTagList);
    }
}