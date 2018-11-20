package com.yang.blog.controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;

import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/file")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 分页查询数据
     *
     * @param page  分页信息
     * @param file 查询对象
     * @return 查询结果
     */
    @RequestMapping("/list")
    public JSONResult list(File file, Page<File> page) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<File> queryWrapper = new QueryWrapper<>();
        //TODO 根据需要决定是否模糊查询，字段值从file中获取。以下注释部分为模糊查询示例，使用时需要注释或删除queryWrapper.setEntity(file);
        //queryWrapper.like("数据库字段1","字段值");
        //queryWrapper.or();
        //queryWrapper.like("数据库字段2","字段值");
        queryWrapper.setEntity(file);
        service.fileService.page(page, queryWrapper);
        jsonResult.success().data(page.getRecords()).count(page.getTotal());
        return jsonResult;
    }

    /**
     * 添加数据
     *
     * @param file 添加对象
     * @return 添加结果
     */
    @RequestMapping("/add")
    public JSONResult add(File file) {
        JSONResult jsonResult = JSONResult.init();
        boolean result = service.fileService.save(file);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 更新数据
     *
     * @param file 更新对象
     * @return 添加结果
     */
    @RequestMapping("/update")
    public JSONResult update(File file) {
        JSONResult jsonResult = JSONResult.init();
        UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
        //TODO 根据需要设置需要更新的列，字段值从file获取。以下注释部分为指定更新列示例，使用时需要注释或删除updateWrapper.setEntity(file);
        //updateWrapper.set("数据库字段1","字段值");
        //updateWrapper.set("数据库字段2","字段值");
        updateWrapper.eq("表示主键的字段","file中表示主键的值");
        boolean result = service.fileService.update(file, updateWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param file 删除对象
     * @param logical 是否逻辑删除。默认false，使用物理删除
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public JSONResult delete(File file, @RequestParam(required = false, defaultValue = "false") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (logical) {
            UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
            //TODO 根据需要修改表示逻辑删除的列和值。
            updateWrapper.set("表示逻辑删除的字段","表示逻辑删除的值");
            result = service.fileService.update(file,updateWrapper);
        } else {
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(file);
            result = service.fileService.remove(queryWrapper);
        }
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}