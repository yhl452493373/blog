<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册 - 闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
        <#include "include.resource.ftl">
</head>
<body class="lay-blog">
        <#include "include.header.ftl">
<div class="container-wrap">
    <div class="container">
        <div class="register-box">
            <form class="layui-form" action="index.html" method="post">
                <div class="layui-form-item">
                    <h3 class="box-title">用户注册</h3>
                </div>
                <div class="layui-form-item">
                    <input type="text" class="layui-input" placeholder="请输入用户名">
                </div>
                <div class="layui-form-item">
                    <input type="text" class="layui-input" placeholder="请输入密码">
                </div>
                <div class="layui-form-item">
                    <input type="text" class="layui-input" placeholder="请确认密码">
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline" style="margin-right: 0">
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" placeholder="请输入验证码">
                        </div>
                        <img src="${contextPath}/captcha?t=" class="captcha" alt="验证码图片"
                             onclick="this.src=this.src.replace(/\?t=\d*/,'?t='+new Date().getTime())">
                    </div>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn layui-btn-normal layui-btn-fluid register-btn">注册</button>
                    <a href="${contextPath}/login" class="layui-btn layui-btn-normal layui-btn-fluid login-btn">登录</a>
                </div>
            </form>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.extend({
        blog: '{/}../res/layui-ext/blog/blog',
    }).use('blog');
</script>
</body>
</html>