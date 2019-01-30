package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.utils.CommonUtils;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.yang.blog.bean.Constant;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.config.SystemProperties;
import com.yang.blog.entity.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class MainController {
    //TODO 调用service的全局对象,代码生成后取消这里的注释,并引入相关类
    private ServiceConfig service = ServiceConfig.serviceConfig;

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        //TODO 生成验证码代码生成后取消这里的注释
        service.shiroCaptcha.generate(request, response);
    }

    @GetMapping("/index")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("index", "selected");
        return "new/index";
    }

    @GetMapping("/article")
    public String article(ModelMap modelMap){
        modelMap.addAttribute("article", "selected");
        return "new/article";
    }

    @GetMapping("/album")
    public String album(ModelMap modelMap){
        modelMap.addAttribute("album", "selected");
        return "new/album";
    }

    @GetMapping("/register")
    public String register(ModelMap modelMap) {
        modelMap.addAttribute("allowRegister", SystemProperties.getAllowRegister());
        return "register";
    }

    @GetMapping("/login")
    public String login(ModelMap modelMap) {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }
        modelMap.addAttribute("allowRegister", SystemProperties.getAllowRegister());
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

    @GetMapping({"/create", "/edit/{articleId}"})
    public String create(@PathVariable(required = false) String articleId, HttpServletRequest request, ModelMap modelMap) {
        //初始时为正常状态
        modelMap.addAttribute("code", Constant.CODE_OK);
        modelMap.addAttribute("codeMap", Constant.getCodeMap());
        //初始时默认article为空
        modelMap.addAttribute("articleEdit", new Article());
        //初始时默认不是草稿
        modelMap.addAttribute("isDraft", false);
        if (request.getRequestURI().contains("/edit/")) {
            if (StringUtils.isEmpty(articleId)) {
                //编辑路径进入,但是没有文章id,则为错误状态
                modelMap.addAttribute("code", Constant.CODE_ID_EMPTY);
            } else {
                Article article = service.articleService.getById(articleId);
                if (article == null) {
                    //为空,则不存在
                    modelMap.addAttribute("code", Constant.CODE_NOT_EXIST);
                } else {
                    //正常
                    //查找使用的文件
                    QueryWrapper<ArticleFile> articleFileQueryWrapper = new QueryWrapper<>();
                    articleFileQueryWrapper.eq("article_id", article.getId());
                    Collection<ArticleFile> articleFileList = service.articleFileService.list(articleFileQueryWrapper);
                    List<String> fileIdList = CommonUtils.convertToFieldList(articleFileList, "getFileId");
                    article.setFileIds(String.join(",", fileIdList));
                    //查找使用的tag
                    initArticleTags(article);
                    modelMap.addAttribute("articleEdit", article);
                }
            }
        } else {
            QueryWrapper<Article> draftQueryWrapper = new QueryWrapper<>();
            draftQueryWrapper.eq("is_draft", Article.IS_DRAFT_TRUE);
            Article draft = service.articleService.getOne(draftQueryWrapper);
            if (draft != null) {
                //查找使用的tag
                initArticleTags(draft);
                modelMap.addAttribute("articleEdit", draft);
                modelMap.addAttribute("isDraft", true);
            }
        }
        modelMap.addAttribute("create", "layui-this");
        return "details.edit";
    }

    @GetMapping("/message")
    public String message(ModelMap modelMap) {
        modelMap.addAttribute("message", "selected");
        return "new/message";
    }

    @GetMapping("/about")
    public String about(ModelMap modelMap) {
        QueryWrapper<About> aboutQueryWrapper = new QueryWrapper<>();
        aboutQueryWrapper.orderByDesc("created_time");
        About about = service.aboutService.getOne(aboutQueryWrapper);
        modelMap.addAttribute("aboutRead", about);
        modelMap.addAttribute("about", "selected");
        return "new/about";
    }

    @GetMapping("/about/edit")
    public String aboutEdit(ModelMap modelMap) {
        QueryWrapper<About> aboutQueryWrapper = new QueryWrapper<>();
        aboutQueryWrapper.orderByDesc("created_time");
        About about = service.aboutService.getOne(aboutQueryWrapper);
        if (about == null) {
            about = new About();
        } else {
            QueryWrapper<AboutFile> aboutFileQueryWrapper = new QueryWrapper<>();
            aboutFileQueryWrapper.eq("about_id", about.getId());
            List<AboutFile> aboutFileList = service.aboutFileService.list(aboutFileQueryWrapper);
            List<String> fileIdList = new ArrayList<>();
            aboutFileList.forEach(aboutFile -> fileIdList.add(aboutFile.getFileId()));
            about.setFileIds(String.join(",", fileIdList));
        }
        modelMap.addAttribute("aboutEdit", about);
        modelMap.addAttribute("about", "layui-this");
        return "about.edit";
    }

    @GetMapping("/details/{articleId}")
    public String details(@PathVariable String articleId, ModelMap modelMap) {
        Article article = service.articleService.getById(articleId);
        if (article == null) {
            modelMap.addAttribute("article", new Article());
            modelMap.addAttribute("tagList", new ArrayList<>());
        } else {
            modelMap.addAttribute("article", article);
            Collection<Tag> tagList = service.tagService.listArticleRelateTagAsc(articleId, "name");
            modelMap.addAttribute("tagList", tagList);
        }
        modelMap.addAttribute("index", "layui-this");
        return "details";
    }

    @GetMapping("/comment/{articleId}")
    public String comment(@PathVariable String articleId, ModelMap modelMap) {
        Article article = service.articleService.getById(articleId);
        modelMap.addAttribute("article", article);
        modelMap.addAttribute("index", "layui-this");
        return "comment";
    }

    @GetMapping("/uploadTest")
    public String uploadTest() {
        return "uploadTest";
    }

    /**
     * 初始化文章的tags
     *
     * @param article 文章
     */
    private void initArticleTags(Article article) {
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("article_id", article.getId());
        Collection<ArticleTag> articleTagList = service.articleTagService.list(articleTagQueryWrapper);
        if (!articleTagList.isEmpty()) {
            List<String> tagIdList = CommonUtils.convertToFieldList(articleTagList, "getTagId");
            Collection<Tag> tagList = service.tagService.listByIds(tagIdList);
            List<String> tagNameList = CommonUtils.convertToFieldList(tagList, "getName");
            article.setTags(String.join(",", tagNameList));
        }
    }
}
