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
        <#if allowRegister>
        <div class="register-box">
            <form id="registerForm" class="layui-form" action="${contextPath}/data/user/register" method="post">
                <div class="layui-form-item">
                    <h3 class="box-title">用户注册</h3>
                </div>
                <div class="layui-form-item">
                    <input name="username" type="text" class="layui-input" placeholder="请输入用户名">
                </div>
                <div class="layui-form-item">
                    <input name="password" type="password" class="layui-input" placeholder="请输入密码">
                </div>
                <div class="layui-form-item">
                    <input name="confirmPassword" type="password" class="layui-input" placeholder="请确认密码">
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline" style="margin-right: 0">
                        <div class="layui-input-inline">
                            <input name="captcha" type="text" class="layui-input" placeholder="请输入验证码">
                        </div>
                        <img src="${contextPath}/captcha?t=" class="captcha" alt="验证码图片" onclick="this.src=this.src.replace(/\?t=\d*/,'?t='+new Date().getTime())">
                    </div>
                </div>
                <div class="layui-form-item">
                    <button href="javascript:void(0)" id="register" class="layui-btn layui-btn-normal layui-btn-fluid register-btn">注册</button>
                    <a href="${contextPath}/login" class="layui-btn layui-btn-normal layui-btn-fluid login-btn">登录</a>
                </div>
            </form>
        </div>
        <#else>
        <script>
            layui.use('layer', function () {
                layui.layer.alert('当前不允许注册', {
                    cancel: function () {
                        window.location.href = contextPath + "/login";
                    }
                }, function () {
                    window.location.href = contextPath + "/login";
                });
            });
        </script>
        </#if>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.use(['jquery', 'layer'], function () {
        var $ = layui.jquery, layer = layui.layer;

        var registerAjax, registerAlert;
        $(document).on('click', '#register', function (e) {
            e.preventDefault();
            register();
        }).on('keydown', function (e) {
            if (e.key === 'Enter') {
                if (registerAjax != null || registerAlert != null) {
                    e.preventDefault();
                    return false;
                }
            }
        });

        function register() {
            var $registerForm = $('#registerForm');
            registerAjax = $.ajax({
                url: $registerForm.attr('action'),
                type: $registerForm.attr('method'),
                data: $registerForm.serialize(),
                success: function (result) {
                    registerAlert = layer.alert(result.message, {
                        success: function () {
                            this.enterEsc = function (event) {
                                if (event.key === 'Enter') {
                                    layer.close(registerAlert);
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
                    }, function (index) {
                        if (result.status === 'success') {
                            window.location.href = contextPath + "/login";
                        } else {
                            $('.captcha').click();
                        }
                        registerAlert = null;
                        layer.close(index);
                    });
                    registerAjax = null;
                },
                error: function () {
                    registerAjax = null;
                }
            })
        }
    });
</script>
</body>
</html>