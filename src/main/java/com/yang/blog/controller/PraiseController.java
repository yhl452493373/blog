package com.yang.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yhl452493373.bean.JSONResult;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.Praise;
import com.yang.blog.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author User
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/data/praise")
public class PraiseController {
    private final Logger logger = LoggerFactory.getLogger(PraiseController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 添加数据
     *
     * @param praise 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(Praise praise) {
        JSONResult jsonResult = JSONResult.init();
        if (SecurityUtils.getSubject().isAuthenticated()) {
            praise.setUserId(ShiroUtils.getLoginUser().getId());
        }
        praise.setCreatedTime(LocalDateTime.now());
        boolean result = service.praiseService.save(praise);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param praise 删除对象
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public JSONResult delete(Praise praise) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<Praise> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(praise);
        boolean result = service.praiseService.remove(queryWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}