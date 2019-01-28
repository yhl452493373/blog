package com.yang.blog.controller;


import com.github.yhl452493373.bean.JSONResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.servlet.view.AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE;

/**
 * 全局处理异常的类，给与用户友好的异常提示
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;

    @ExceptionHandler(value = Exception.class)
    public void defaultHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        //打印出错误信息到控制台
        logger.error("", exception);
        JSONResult jsonResult = JSONResult.init();
        if (exception instanceof MaxUploadSizeExceededException) {
            MaxUploadSizeExceededException me = (MaxUploadSizeExceededException) exception;
            jsonResult.error("上传文件过大:" + exception.toString()).code(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE).detail(getExceptionAllInformation(me));
        } else if (exception instanceof AuthorizationException) {
            AuthorizationException ae = (AuthorizationException) exception;
            jsonResult.error("无权访问:" + exception.toString()).code(HttpServletResponse.SC_UNAUTHORIZED).detail(getExceptionAllInformation(ae));
        } else {
            jsonResult.error("内部错误:" + exception.toString()).code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).detail(getExceptionAllInformation(exception));
        }
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("utf-8");
        if (writer != null) {
            String accept = request.getHeader("Accept");
            if (accept != null && accept.contains("html") || !"XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
                //如果要求返回html或者不是ajax请求
                response.setContentType("text/html; charset=utf-8");
                try {
                    Configuration configuration = freeMarkerConfigurer.getConfiguration();
                    Map<String, Object> model = new HashMap<>();
                    jsonResult.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE,new RequestContext(request, response, request.getServletContext(), model));
                    //渲染错误页面
                    Template template = configuration.getTemplate("error.ftl");
                    template.process(jsonResult, writer);
                } catch (IOException | TemplateException e) {
                    e.printStackTrace();
                }
            } else {
                //是ajax请求或者没有说要求什么
                response.setContentType("application/json; charset=utf-8");
                try {
                    writer.write(jsonResult.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取异常的详细信息
     *
     * @param t 异常
     * @return 详细信息字符串
     */
    private static String getExceptionAllInformation(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}