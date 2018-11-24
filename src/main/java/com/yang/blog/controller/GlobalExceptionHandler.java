package com.yang.blog.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.yhl452493373.bean.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局处理异常的类，给与用户友好的异常提示
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JSONObject defaultHandler(HttpServletRequest request , Exception e) {
        Boolean layEditUpload = Boolean.valueOf(request.getParameter("layEditUpload"));
        JSONResult jsonResult = JSONResult.init();
        JSONObject jsonObject = new JSONObject();
        if (e instanceof MaxUploadSizeExceededException) {
            MaxUploadSizeExceededException maxUploadSizeExceededException = (MaxUploadSizeExceededException) e;
            if (layEditUpload) {
                jsonObject.put("code", HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
                jsonObject.put("msg", "上传文件过大!");
                return jsonObject;
            } else {
                return jsonResult.error("上传文件过大!").code(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE).detail(maxUploadSizeExceededException.getMessage());
            }
        }
        logger.error("内部错误:", e);
        if (layEditUpload) {
            jsonObject.put("code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonObject.put("msg", "内部错误." + e.getMessage());
            return jsonObject;
        } else {
            return jsonResult.error("内部错误.").count(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).detail(e.getMessage());
        }
    }
}