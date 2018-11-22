package com.yang.blog.controller;


import com.github.yhl452493373.bean.JSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 全局处理异常的类，给与用户友好的异常提示
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JSONResult defaultHandler(Exception e) {
        JSONResult jsonResult = JSONResult.init();
        if (e instanceof MaxUploadSizeExceededException) {
            MaxUploadSizeExceededException maxUploadSizeExceededException = (MaxUploadSizeExceededException) e;
            return jsonResult.error("上传文件过大!").detail(maxUploadSizeExceededException.getMessage());
        }
        logger.error("内部错误:", e);
        return jsonResult.error("内部错误.").detail(e.getMessage());
    }
}