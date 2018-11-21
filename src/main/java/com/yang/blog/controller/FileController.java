package com.yang.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.yang.blog.bean.MultipartFileParam;
import com.yang.blog.util.FileUploadUtil;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;

import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/file")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    @PostMapping("/upload")
    public JSONObject upload(MultipartFileParam fileParam, @RequestParam(required = false, defaultValue = "false") Boolean layEditUpload, HttpServletRequest request) {
        //todo 待测试断点上传
        JSONResult jsonResult = JSONResult.init();
        // 判断前端Form表单格式是否支持文件上传
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            if (layEditUpload) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 500);
                jsonObject.put("msg", "不支持的表单格式");
                return jsonObject;
            }
            return jsonResult.error("不支持的表单格式");
        }
        logger.info("上传文件开始");
        try {
            java.io.File uploadedFile = FileUploadUtil.chunkUploadByMappedByteBuffer(fileParam);
            if (uploadedFile != null) {
                jsonResult.success("文件上传成功。");
                if (layEditUpload) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 0);
                    jsonObject.put("msg", "文件上传成功");
                    JSONObject dataJSONObject = new JSONObject();
                    dataJSONObject.put("src", "imgsrc");
                    dataJSONObject.put("title", "imgtitle");
                    jsonObject.put("data", dataJSONObject);
                    return jsonObject;
                }
            }
        } catch (IOException e) {
            logger.error("文件上传失败。{}", fileParam.toString());
            if (layEditUpload) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", 500);
                jsonObject.put("msg", "文件上传失败");
                return jsonObject;
            }
            jsonResult.error("文件上传失败。");
        }
        logger.info("上传文件结束");
        return jsonResult;
    }

    /**
     * 分页查询数据
     *
     * @param page 分页信息
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
        updateWrapper.eq("表示主键的字段", "file中表示主键的值");
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
     * @param file    删除对象
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
            updateWrapper.set("表示逻辑删除的字段", "表示逻辑删除的值");
            result = service.fileService.update(file, updateWrapper);
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