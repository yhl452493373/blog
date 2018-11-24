package com.yang.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.bean.MultipartFileParam;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.File;
import com.yang.blog.shiro.ShiroUtils;
import com.yang.blog.util.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author User
 * @since 2018-11-20
 */
@RestController
@RequestMapping("/data/file")
public class FileController implements BaseController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 单文件上传
     *
     * @param fileParam     文件上传信息
     * @param layEditUpload 是否layEdit的文件上传。layEdit文件上传返回值与其他的不一样。返回{code:0,msg:'消息',data:{src:'路径',titile:'文件名',fileId:'文件id'}}。其他返回JSONResult格式，在其data中有fileId表示文件id，fileUrl表示文件下载路径
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
            //分片上传时，判断文件的大小是否超过服务器设置的单个文件大小
            if (fileParam.getChunkTotal() != 0 && service.getMaxFileSize().compareTo(DataSize.ofBytes(fileParam.getFileSize())) < 0) {
                throw new MaxUploadSizeExceededException(fileParam.getFileSize());
            }
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
                //此处上传的所有文件都是临时状态，与其关联的对象添加后，要修改这个文件的状态
                file.setAvailable(File.TEMP);
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
                    dataJSONObject.put("fileId", file.getId());
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
        if (file.getAvailable().equals(File.DELETE)) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            try (PrintWriter writer = response.getWriter()) {
                jsonResult.error("下载失败,文件已删除").code(HttpServletResponse.SC_NOT_FOUND);
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
     * @param fileIds       删除对象id,多个id用逗号分隔
     * @param logical       是否逻辑删除。默认false，使用物理删除
     * @param layEditDelete 是否layEdit的文件删除
     * @return 删除结果。data为删除的文件id数组
     */
    @RequestMapping("/delete")
    public JSONResult delete(String fileIds, HttpServletRequest request, @RequestParam(required = false, defaultValue = "false") Boolean logical, @RequestParam(defaultValue = "false", required = false) Boolean layEditDelete) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (!layEditDelete) {
            List<String> fileIdList = CommonUtils.splitIds(fileIds);
            Collection<File> fileList = service.fileService.listByIds(fileIdList);
            if (logical) {
                fileList.forEach(file -> file.setAvailable(File.DELETE));
                result = service.fileService.updateBatchById(fileList);
            } else {
                fileList.forEach(file -> {
                    java.io.File uploadFile = new java.io.File(FileUtils.uploadPath(file));
                    uploadFile.delete();
                });
                service.fileService.removeByIds(CommonUtils.convertToIdList(fileList));
                result = true;
            }
            if (result)
                jsonResult.data(fileIdList);
        } else {
            result = true;
            String imagePath = request.getParameter("imgpath");
            String videoPath = request.getParameter("filepath");
            List<String> fileIdList = new ArrayList<>();
            if (StringUtils.isNotEmpty(imagePath)) {
                String imageId = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                FileUtils.delete(imageId);
                fileIdList.add(imageId);
            }
            if (StringUtils.isNotEmpty(videoPath)) {
                String videoId = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                FileUtils.delete(videoId);
                fileIdList.add(videoId);
            }
            jsonResult.data(fileIdList);
        }
        if (result)
            jsonResult.success(DELETE_SUCCESS);
        else
            jsonResult.error(DELETE_FAILED);
        return jsonResult;
    }
}