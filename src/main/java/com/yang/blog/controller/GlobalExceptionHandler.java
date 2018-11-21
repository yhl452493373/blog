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
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public JSONResult maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        logger.error("上传文件过大:", e);
        return JSONResult.init().error("上传文件过大！").detail(e.getMessage());
    }
}