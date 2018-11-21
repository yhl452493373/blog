package com.yang.blog.controller;

import com.yang.blog.config.ServiceConfig;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public String index() {
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
}
