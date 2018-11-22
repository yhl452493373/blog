package com.yang.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;
import com.yang.blog.bean.MultipartFileParam;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.File;
import com.yang.blog.shiro.ShiroUtils;
import com.yang.blog.util.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/file")
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 单文件上传
     */
    @PostMapping("/upload")
    public JSONObject upload(MultipartFileParam fileParam, @RequestParam(required = false, defaultValue = "false") Boolean layEditUpload, HttpServletRequest request) {
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
            Map<String, Object> uploadMap = FileUtils.upload(fileParam);
            if (uploadMap.get("file") != null) {
                java.io.File uploadFile = (java.io.File) uploadMap.get("file");
                File file = new File();
                file.setFileType(fileParam.getFileType());
                file.setOriginalName(fileParam.getFileName());
                file.setSaveName(uploadFile.getName());
                file.setExtensionName(uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".")));
                file.setCreatedTime(LocalDateTime.now());
                file.setSize(uploadFile.length());
                file.setUserId(ShiroUtils.getLoginUser().getId());
                file.setAvailable(File.BLOCK);
                //去掉保存的文件名和原始文件名的后缀名
                file.setOriginalName(file.getOriginalName().substring(0, file.getOriginalName().lastIndexOf(".")));
                file.setSaveName(file.getSaveName().substring(0, file.getSaveName().lastIndexOf(".")));
                service.fileService.save(file);
                uploadMap.remove("file");
                uploadMap.remove("taskId");
                uploadMap.put("fileId", file.getId());
                uploadMap.put("fileUrl", "/data/file/download/" + file.getId());
                jsonResult.success("文件上传成功.").data(uploadMap);
                if (layEditUpload) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", 0);
                    jsonObject.put("msg", "文件上传成功");
                    JSONObject dataJSONObject = new JSONObject();
                    dataJSONObject.put("src", "/data/file/download/" + file.getId());
                    dataJSONObject.put("title", file.getOriginalName());
                    jsonObject.put("data", dataJSONObject);
                    return jsonObject;
                }
            } else {
                uploadMap.remove("file");
                jsonResult.success("文件上传中.").data(uploadMap);
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

    @GetMapping("/download/{fileId}")
    public void download(@PathVariable String fileId, HttpServletRequest request, HttpServletResponse response) {
        File file = service.fileService.getById(fileId);
        JSONResult jsonResult = JSONResult.init();
        if (file == null) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            try (PrintWriter writer = response.getWriter()) {
                jsonResult.error("下载失败,文件记录未找到").code(HttpServletResponse.SC_NOT_FOUND);
                writer.write(jsonResult.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        java.io.File downloadFile = new java.io.File(FileUtils.uploadPath(file));
        if (!downloadFile.exists()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            try (PrintWriter writer = response.getWriter()) {
                jsonResult.error("下载失败,文件记录对应的文件未找到").code(HttpServletResponse.SC_NOT_FOUND);
                writer.write(jsonResult.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        FileUtils.download(request, response, downloadFile, file.getOriginalName() + file.getExtensionName());
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
    public JSONResult delete(File file, HttpServletRequest request, @RequestParam(required = false, defaultValue = "false") Boolean logical, @RequestParam(defaultValue = "false", required = false) Boolean layEditDelete) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (!layEditDelete) {
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
        } else {
            result = true;
            String imgPath = request.getParameter("imgpath");
            String filePath = request.getParameter("imgpath");
            if (StringUtils.isNotEmpty(imgPath)) {
                String imgId = imgPath.substring(imgPath.lastIndexOf("/"));
            }
            if (StringUtils.isNotEmpty(filePath)) {
                String videoId = filePath.substring(filePath.lastIndexOf("/"));
            }
        }
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}