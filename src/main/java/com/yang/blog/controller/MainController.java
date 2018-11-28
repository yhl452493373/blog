package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.bean.Constant;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.config.SystemProperties;
import com.yang.blog.entity.*;
import com.yang.blog.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        //TODO 生成验证码代码生成后取消这里的注释
        service.shiroCaptcha.generate(request, response);
    }

    @GetMapping("/index")
    public String index(ModelMap modelMap) {
        modelMap.addAttribute("index", "layui-this");
        return "index";
    }

    @GetMapping("/register")
    public String register(ModelMap modelMap) {
        modelMap.addAttribute("allowRegister", SystemProperties.getAllowRegister());
        return "register";
    }

    @GetMapping("/login")
    public String login(ModelMap modelMap) {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/login";
        }
        modelMap.addAttribute("allowRegister", SystemProperties.getAllowRegister());
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/index";
    }

    @GetMapping({"/create", "/edit/{articleId}"})
    public String create(@PathVariable(required = false) String articleId, HttpServletRequest request, ModelMap modelMap) {
        //初始时为正常状态
        modelMap.addAttribute("code", Constant.CODE_OK);
        modelMap.addAttribute("codeMap", Constant.getCodeMap());
        //初始时默认article为空
        modelMap.addAttribute("articleEdit", new Article());
        if (request.getRequestURI().contains("/edit/")) {
            if (StringUtils.isEmpty(articleId)) {
                //编辑路径进入,但是没有文章id,则为错误状态
                modelMap.addAttribute("code", Constant.CODE_ID_EMPTY);
            } else {
                User user = ShiroUtils.getLoginUser();
                Article article = service.articleService.getById(articleId);
                if (article == null) {
                    //为空,则不存在
                    modelMap.addAttribute("code", Constant.CODE_NOT_EXIST);
                } else {
                    if (!article.getUserId().equals(user.getId())) {
                        //用户id不是登录用户id,则无权
                        modelMap.addAttribute("code", Constant.CODE_NO_PERMISSION);
                    } else {
                        //正常
                        //查找使用的文件
                        QueryWrapper<ArticleFile> articleFileQueryWrapper = new QueryWrapper<>();
                        articleFileQueryWrapper.eq("article_id", article.getId());
                        articleFileQueryWrapper.eq("user_id", user.getId());
                        Collection<ArticleFile> articleFileList = service.articleFileService.list(articleFileQueryWrapper);
                        List<String> fileIdList = CommonUtils.convertToFieldList(articleFileList, "getFileId");
                        article.setFileIds(String.join(",", fileIdList));
                        //查找使用的tag
                        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
                        articleTagQueryWrapper.eq("article_id", article.getId());
                        articleTagQueryWrapper.eq("user_id", user.getId());
                        Collection<ArticleTag> articleTagList = service.articleTagService.list(articleTagQueryWrapper);
                        if (!articleTagList.isEmpty()) {
                            List<String> tagIdList = CommonUtils.convertToFieldList(articleTagList, "getTagId");
                            Collection<Tag> tagList = service.tagService.listByIds(tagIdList);
                            List<String> tagNameList = CommonUtils.convertToFieldList(tagList, "getName");
                            article.setTags(String.join(",", tagNameList));
                        }
                        modelMap.put("articleEdit", article);
                    }
                }
            }
        }
        modelMap.addAttribute("create", "layui-this");
        return "details.edit";
    }

    @GetMapping("/message")
    public String message(ModelMap modelMap) {
        modelMap.addAttribute("message", "layui-this");
        return "message";
    }

    @GetMapping("/about")
    public String about(ModelMap modelMap) {
        QueryWrapper<About> aboutQueryWrapper = new QueryWrapper<>();
        aboutQueryWrapper.orderByDesc("created_time");
        About about = service.aboutService.getOne(aboutQueryWrapper);
        modelMap.addAttribute("aboutRead", about);
        modelMap.addAttribute("about", "layui-this");
        return "about";
    }

    @GetMapping("/about/edit")
    public String aboutEdit(ModelMap modelMap) {
        QueryWrapper<About> aboutQueryWrapper = new QueryWrapper<>();
        User user = ShiroUtils.getLoginUser();
        aboutQueryWrapper.ge("user_id", user.getId());
        aboutQueryWrapper.orderByDesc("created_time");
        About about = service.aboutService.getOne(aboutQueryWrapper);
        if (about == null) {
            about = new About();
        } else {
            QueryWrapper<AboutFile> aboutFileQueryWrapper = new QueryWrapper<>();
            aboutFileQueryWrapper.eq("user_id", user.getId());
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
        } else {
            article.setReadCount(article.getReadCount() + 1);
            service.articleService.updateById(article);
            modelMap.addAttribute("article", article);
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
}
