package com.yang.blog.controller;

import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CookieUtils;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.config.SystemConfig;
import com.yang.blog.entity.User;
import com.yang.blog.exception.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
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

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JSONResult register(User user, HttpServletRequest request, HttpServletResponse response) {
        JSONResult json = JSONResult.init();
        try {
            boolean captcha = service.shiroCaptcha.validate(request, response, user.getCaptcha());
            if (!captcha) {
                json.error("验证码不正确");
            } else {
                service.userService.register(user);
                json.success("注册成功");
            }
        } catch (UsernameNullException e) {
            json.error("用户名不能为空");
        } catch (UsernameExistException e) {
            json.error("用户名已经存在");
        } catch (UsernameLengthException e) {
            json.error("用户名长度只能在" + SystemConfig.getUsernameLength().getMin() + "到" + SystemConfig.getUsernameLength().getMax() + "之间");
        } catch (PasswordNullException e) {
            json.error("密码不能为空");
        } catch (PasswordLengthException e) {
            json.error("密码长度只能在" + SystemConfig.getPasswordLength().getMin() + "到" + SystemConfig.getPasswordLength().getMax() + "之间");
        } catch (PasswordNotSameException e) {
            json.error("两次输入密码不一致");
        } catch (RuntimeException e) {
            e.printStackTrace();
            json.error("未知错误，请联系管理员");
        }
        return json;
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        if (request.getMethod().equalsIgnoreCase("get")) {
            return "login";
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
