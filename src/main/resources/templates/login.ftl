<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录 - 闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
</head>
<body class="lay-blog">
<div class="header">
    <div class="header-wrap">
        <h1 class="logo pull-left">
            <a href="${contextPath}/index">
                <img src="${contextPath}/static/images/logo.png" alt="" class="logo-img">
                <img src="${contextPath}/static/images/logo-text.png" alt="" class="logo-text">
            </a>
        </h1>
    </div>
</div>
<div class="container-wrap">
    <div class="container">
        <div class="login-box">
            <form id="loginForm" class="layui-form" action="${contextPath}/data/user/login" method="post">
                <div class="layui-form-item">
                    <h3 class="box-title">用户登录</h3>
                </div>
                <div class="layui-form-item">
                    <input name="username" type="text" class="layui-input" placeholder="请输入用户名">
                </div>
                <div class="layui-form-item">
                    <input name="password" type="text" class="layui-input" placeholder="请输入密码">
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline" style="margin-right: 0">
                        <div class="layui-input-inline">
                            <input name="captcha" type="text" class="layui-input" placeholder="请输入验证码">
                        </div>
                        <img src="${contextPath}/captcha?t=" class="captcha" alt="验证码图片"
                             onclick="this.src=this.src.replace(/\?t=\d*/,'?t='+new Date().getTime())">
                    </div>
                </div>
                <div class="layui-form-item">
                    <button id="login" class="layui-btn layui-btn-normal layui-btn-fluid login-btn">登录</button>
                    <a href="${contextPath}/register"
                       class="layui-btn layui-btn-normal layui-btn-fluid register-btn">注册</a>
                </div>
            </form>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.use(['jquery', 'layer'], function () {
        var $ = layui.jquery, layer = layui.layer;

        $(document).on('click', '#login', function (e) {
            e.preventDefault();
            login();
        });

        function login() {
            var $loginForm = $('#loginForm');
            $.ajax({
                url: $loginForm.attr('action'),
                type: $loginForm.attr('method'),
                data: $loginForm.serialize(),
                success: function (result) {
                    layer.alert(result.message, function (index) {
                        if (result.status === 'success') {
                            window.location.reload();
                        } else {
                            $('.captcha').click();
                        }
                        layer.close(index);
                    });
                    console.log(result);
                }
            })
        }
    });
</script>
</body>
</html>