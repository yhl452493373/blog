package com.yang.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yhl452493373.bean.JSONResult;
import com.github.yhl452493373.utils.CommonUtils;
import com.yang.blog.bean.MultipartFileParam;
import com.yang.blog.config.ServiceConfig;
import com.yang.blog.entity.File;
import com.yang.blog.util.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

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
     * @param fileParam 文件上传信息
     */
    @PostMapping("/upload")
    public JSONObject upload(MultipartFileParam fileParam, HttpServletRequest request) {
        JSONResult jsonResult = JSONResult.init();
        String contextPath = request.getContextPath();
        // 判断前端Form表单格式是否支持文件上传
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            return jsonResult.code(HttpServletResponse.SC_NOT_ACCEPTABLE).error("不支持的表单格式");
        }
        logger.info("上传文件开始");
        try {
            //分片上传时，判断文件的大小是否超过服务器设置的单个文件大小
            if (fileParam.getIsChunk()) {
                if (fileParam.getChunk() == MultipartFileParam.PRE_UPLOAD_CHUNK) {
                    if (service.getMaxFileSize().compareTo(DataSize.ofBytes(fileParam.getFileSize())) < 0) {
                        throw new MaxUploadSizeExceededException(fileParam.getFileSize());
                    } else {
                        return jsonResult.success("文件符合要求,允许上传");
                    }
                }
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
                //此处上传的所有文件都是临时状态，与其关联的对象添加后，要修改这个文件的状态
                file.setAvailable(File.TEMP);
                //去掉保存的文件名和原始文件名的后缀名
                file.setOriginalName(file.getOriginalName().substring(0, file.getOriginalName().lastIndexOf(".")));
                file.setSaveName(file.getSaveName().substring(0, file.getSaveName().lastIndexOf(".")));
                service.fileService.save(file);
                uploadMap.remove("file");
                uploadMap.remove("taskId");
                uploadMap.put("fileId", file.getId());
                uploadMap.put("fileUrl", contextPath + "/data/file/download/" + file.getId());
                jsonResult.success("文件上传成功.").data(uploadMap);
                jsonResult.put("link", uploadMap.get("fileUrl"));
            } else {
                uploadMap.remove("file");
                jsonResult.success("文件上传中.").data(uploadMap);
            }
        } catch (IOException e) {
            logger.error("文件上传失败。{}", fileParam.toString());
            jsonResult.code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).error("文件上传失败。");
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
     * @param fileIds   删除对象id,多个id用逗号分隔
     * @param temporary 是否设置为临时文件,主要用于富文本等涉及到图片修改的地方.此时先将图片设置为临时,最后保存时再将使用的文件修改为正常状态.临时文件会被定时清除
     * @param logical   是否逻辑删除。默认false，使用物理删除
     * @return 删除结果。data为删除的文件id数组
     */
    @RequestMapping("/delete")
    public JSONResult delete(String fileIds, HttpServletRequest request, @RequestParam(required = false, defaultValue = "false") Boolean temporary, @RequestParam(required = false, defaultValue = "false") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result = false;
        List<String> fileIdList;
        //fileIds为空,则是从froala editor发起的删除请求
        if (StringUtils.isEmpty(fileIds)) {
            String sourceUrls = request.getParameter("sourceUrls");
            if (sourceUrls != null) {
                fileIdList = new ArrayList<>();
                result = deleteFromSourceUrls(temporary, sourceUrls, fileIdList);
                if (result)
                    jsonResult.data(fileIdList);
            }
        } else {
            fileIdList = CommonUtils.splitIds(fileIds);
            Collection<File> fileList = service.fileService.listByIds(fileIdList);
            if (temporary) {
                //文件设为临时状态
                fileList.forEach(file -> file.setAvailable(File.TEMP));
                result = service.fileService.updateBatchById(fileList);
            } else if (logical) {
                //文件设为删除状态
                fileList.forEach(file -> file.setAvailable(File.DELETE));
                result = service.fileService.updateBatchById(fileList);
            } else {
                //文件物理删除
                fileList.forEach(file -> {
                    java.io.File uploadFile = new java.io.File(FileUtils.uploadPath(file));
                    uploadFile.delete();
                });
                service.fileService.removeByIds(CommonUtils.convertToIdList(fileList));
                result = true;
            }
            if (result)
                jsonResult.data(fileIdList);
        }
        if (result)
            jsonResult.success(DELETE_SUCCESS);
        else
            jsonResult.error(DELETE_FAILED);
        return jsonResult;
    }

    /**
     * 编辑文件时的删除处理.在新增时,文件不使用临时删除,修改时使用
     *
     * @param temporary  是否设置为临时.
     * @param sourceUrls 文件下载路径
     * @param fileIdList 文件id列表
     * @return 是否删除成功
     */
    private boolean deleteFromSourceUrls(@RequestParam(required = false, defaultValue = "false") Boolean temporary, String sourceUrls, List<String> fileIdList) {
        boolean result = false;
        if (StringUtils.isNotEmpty(sourceUrls)) {
            String[] sourceUrlArray = sourceUrls.split(",");
            for (String sourceUrl : sourceUrlArray) {
                String fileId = sourceUrl.substring(sourceUrl.lastIndexOf("/") + 1);
                if (temporary) {
                    result = service.fileService.setAvailable(Collections.singletonList(fileId), File.TEMP);
                    fileIdList.add(fileId);
                } else {
                    FileUtils.delete(fileId);
                    fileIdList.add(fileId);
                }
            }
        }
        return result;
    }
}