package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.About;
import com.yang.blog.entity.AboutFile;
import com.yang.blog.entity.Article;
import com.yang.blog.entity.User;
import com.yang.blog.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/create")
    public String create(ModelMap modelMap) {
        modelMap.addAttribute("create", "layui-this");
        return "create";
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
        article.setReadCount(article.getReadCount() + 1);
        service.articleService.updateById(article);
        modelMap.addAttribute("article", article);
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
