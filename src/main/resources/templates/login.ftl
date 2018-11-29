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
                    <input name="password" type="password" class="layui-input" placeholder="请输入密码">
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
                    <#if allowRegister>
                        <a href="${contextPath}/register" class="layui-btn layui-btn-normal layui-btn-fluid register-btn">注册</a>
                    </#if>
                </div>
            </form>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.use(['jquery', 'layer'], function () {
        var $ = layui.jquery, layer = layui.layer;

        var loginAjax, loginAlert;
        $(document).on('click', '#login', function (e) {
            e.preventDefault();
            login();
        }).on('keydown', function (e) {
            if (e.key === 'Enter') {
                if (loginAjax != null || loginAlert != null) {
                    e.preventDefault();
                    return false;
                }
            }
        });

        function login() {
            var $loginForm = $('#loginForm');
            loginAjax = $.ajax({
                url: $loginForm.attr('action'),
                type: $loginForm.attr('method'),
                data: $loginForm.serialize(),
                success: function (result) {
                    loginAlert = layer.alert(result.message, {
                        success: function () {
                            this.enterEsc = function (event) {
                                if (event.key === 'Enter') {
                                    layer.close(loginAlert);
                                    if (result.status === 'success') {
                                        window.location.reload();
                                    }
                                }
                                return false;
                            };
                            $(document).on('keydown', this.enterEsc);	//监听键盘事件，关闭层
                        },
                        end: function () {
                            $(document).off('keydown', this.enterEsc);	//解除键盘关闭事件
                        }
                    }, function (index, layero) {
                        if (result.status === 'success') {
                            window.location.reload();
                        } else {
                            $('.captcha').click();
                        }
                        loginAlert = null;
                        layer.close(index);
                    });
                    loginAjax = null;
                },
                error: function () {
                    loginAjax = null;
                }
            })
        }
    });
</script>
</body>
</html>