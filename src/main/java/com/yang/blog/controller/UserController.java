package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.config.SystemProperties;
import com.yang.blog.entity.User;
import com.yang.blog.exception.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    @PostMapping("/register")
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
            json.error("用户名长度只能在" + SystemProperties.getUsernameLength().getMin() + "到" + SystemProperties.getUsernameLength().getMax() + "之间");
        } catch (PasswordNullException e) {
            json.error("密码不能为空");
        } catch (PasswordLengthException e) {
            json.error("密码长度只能在" + SystemProperties.getPasswordLength().getMin() + "到" + SystemProperties.getPasswordLength().getMax() + "之间");
        } catch (PasswordNotSameException e) {
            json.error("两次输入密码不一致");
        } catch (RuntimeException e) {
            e.printStackTrace();
            json.error("未知错误，请联系管理员");
        }
        return json;
    }

    @PostMapping("/login")
    public JSONResult login(User user, HttpServletRequest request, HttpServletResponse response) {
        JSONResult json = JSONResult.init();
        Subject subject = SecurityUtils.getSubject();
        logger.info("------------------");
        logger.info("{}->登录", user.getUsername());
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            //非ssoKey跳转,则验证验证码,是否启用记住我
            if (StringUtils.isEmpty(user.getCaptcha())) {
                throw new CaptchaNullException();
            }
            if (!service.shiroCaptcha.validate(request, response, user.getCaptcha())) {
                throw new CaptchaErrorException();
            }
            String isRememberMe = user.getIsRememberMe();
            if (isRememberMe != null && isRememberMe.equals("remember")) {
                usernamePasswordToken.setRememberMe(true);
                logger.info("启用记住我");
            }
            subject.login(usernamePasswordToken);
        } catch (CaptchaNullException e) {
            json.error("验证码为空");
        } catch (CaptchaErrorException e) {
            json.error("验证码错误");
        } catch (UsernameNullException e) {
            json.error("用户名为空");
        } catch (PasswordNullException e) {
            json.error("密码为空");
        } catch (UnknownAccountException e) {
            json.error("用户名不存在");
        } catch (DisabledAccountException e) {
            json.error("用户不可用");
        } catch (IncorrectCredentialsException e) {
            json.error("密码不正确");
        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.warn("未知错误，请联系管理员");
            json.error("未知错误，请联系管理员");
        }
        if (!subject.isAuthenticated()) {
            logger.info("{}->登录失败", user.getUsername());
        } else {
            logger.info("{}->登录成功", user.getUsername());
            json.success("登录成功");
        }
        logger.info("------------------");
        return json;
    }

    /**
     * 分页查询数据
     *
     * @param page 分页信息
     * @param user 查询对象
     * @return 查询结果
     */
    @RequestMapping("/list")
    public JSONResult list(User user, Page<User> page) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //TODO 根据需要决定是否模糊查询，字段值从user中获取。以下注释部分为模糊查询示例，使用时需要注释或删除queryWrapper.setEntity(user);
        //queryWrapper.like("数据库字段1","字段值");
        //queryWrapper.or();
        //queryWrapper.like("数据库字段2","字段值");
        queryWrapper.setEntity(user);
        service.userService.page(page, queryWrapper);
        jsonResult.success().data(page.getRecords()).count(page.getTotal());
        return jsonResult;
    }

    /**
     * 添加数据
     *
     * @param user 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(User user) {
        JSONResult jsonResult = JSONResult.init();
        boolean result = service.userService.save(user);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 更新数据
     *
     * @param user 更新对象
     * @return 添加结果
     */
    @RequestMapping("/update")
    public JSONResult update(User user) {
        JSONResult jsonResult = JSONResult.init();
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        //TODO 根据需要设置需要更新的列，字段值从user获取。以下注释部分为指定更新列示例，使用时需要注释或删除updateWrapper.setEntity(user);
        //updateWrapper.set("数据库字段1","字段值");
        //updateWrapper.set("数据库字段2","字段值");
        updateWrapper.eq("表示主键的字段", "user中表示主键的值");
        boolean result = service.userService.update(user, updateWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param user    删除对象
     * @param logical 是否逻辑删除。默认false，使用物理删除
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public JSONResult delete(User user, @RequestParam(required = false, defaultValue = "false") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (logical) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            //TODO 根据需要修改表示逻辑删除的列和值。
            updateWrapper.set("表示逻辑删除的字段", "表示逻辑删除的值");
            result = service.userService.update(user, updateWrapper);
        } else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(user);
            result = service.userService.remove(queryWrapper);
        }
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}