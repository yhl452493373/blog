package com.yang.blog.controller;

import com.yang.blog.config.ServiceConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {
    //TODO 调用service的全局对象,代码生成后取消这里的注释,并引入相关类
    private ServiceConfig serviceConfig = ServiceConfig.serviceConfig;

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        //TODO 生成验证码代码生成后取消这里的注释
        serviceConfig.shiroCaptcha.generate(request, response);
    }

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/register")
    public String register(){
        return "/register";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase("get")) {
            return "/login";
        } else {
            //TODO 验证验证码是否正确,代码生成后取消此处注释
//            serviceConfig.shiroCaptcha.validate(request,response,request.getParameter("captcha"));
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
            usernamePasswordToken.setUsername("admin");
            usernamePasswordToken.setPassword("admin".toCharArray());
            usernamePasswordToken.setRememberMe(false);
            SecurityUtils.getSubject().login(usernamePasswordToken);
            if (SecurityUtils.getSubject().isAuthenticated())
                return "redirect:/index";
            return "redirect:/login";
        }
    }
}
