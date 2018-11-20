package com.yang.blog.controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;

import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.About;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/about")
public class AboutController {
    private final Logger logger = LoggerFactory.getLogger(AboutController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 分页查询数据
     *
     * @param page  分页信息
     * @param about 查询对象
     * @return 查询结果
     */
    @RequestMapping("/list")
    public JSONResult list(About about, Page<About> page) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<About> queryWrapper = new QueryWrapper<>();
        //TODO 根据需要决定是否模糊查询，字段值从about中获取。以下注释部分为模糊查询示例，使用时需要注释或删除queryWrapper.setEntity(about);
        //queryWrapper.like("数据库字段1","字段值");
        //queryWrapper.or();
        //queryWrapper.like("数据库字段2","字段值");
        queryWrapper.setEntity(about);
        service.aboutService.page(page, queryWrapper);
        jsonResult.success().data(page.getRecords()).count(page.getTotal());
        return jsonResult;
    }

    /**
     * 添加数据
     *
     * @param about 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(About about) {
        JSONResult jsonResult = JSONResult.init();
        boolean result = service.aboutService.save(about);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 更新数据
     *
     * @param about 更新对象
     * @return 添加结果
     */
    @RequestMapping("/update")
    public JSONResult update(About about) {
        JSONResult jsonResult = JSONResult.init();
        UpdateWrapper<About> updateWrapper = new UpdateWrapper<>();
        //TODO 根据需要设置需要更新的列，字段值从about获取。以下注释部分为指定更新列示例，使用时需要注释或删除updateWrapper.setEntity(about);
        //updateWrapper.set("数据库字段1","字段值");
        //updateWrapper.set("数据库字段2","字段值");
        updateWrapper.eq("表示主键的字段","about中表示主键的值");
        boolean result = service.aboutService.update(about, updateWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param about 删除对象
     * @param logical 是否逻辑删除。默认false，使用物理删除
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public JSONResult delete(About about, @RequestParam(required = false, defaultValue = "false") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (logical) {
            UpdateWrapper<About> updateWrapper = new UpdateWrapper<>();
            //TODO 根据需要修改表示逻辑删除的列和值。
            updateWrapper.set("表示逻辑删除的字段","表示逻辑删除的值");
            result = service.aboutService.update(about,updateWrapper);
        } else {
            QueryWrapper<About> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(about);
            result = service.aboutService.remove(queryWrapper);
        }
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}