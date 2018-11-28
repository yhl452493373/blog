<#include "include.common.ftl">
<div class="header">
    <div class="header-wrap">
        <h1 class="logo pull-left">
            <a href="${contextPath}/index">
                <img src="${contextPath}/static/images/logo.png" alt="" class="logo-img">
                <img src="${contextPath}/static/images/logo-text.png" alt="" class="logo-text">
            </a>
        </h1>
        <form class="layui-form blog-seach pull-left" action="">
            <div class="layui-form-item blog-sewrap">
                <div class="layui-input-block blog-sebox">
                    <i class="layui-icon layui-icon-search"></i>
                    <input type="text" name="title" lay-verify="title" autocomplete="off" class="layui-input">
                </div>
            </div>
        </form>
        <div class="blog-nav pull-right">
            <ul class="layui-nav pull-left">
                <li class="layui-nav-item ${index!''}"><a href="${contextPath}/index">文章</a></li>
                <@shiro.user>
                    <li class="layui-nav-item ${create!''}"><a href="${contextPath}/create">撰写</a></li>
                </@shiro.user>
                <li class="layui-nav-item ${message!''}"><a href="${contextPath}/message">留言</a></li>
                <li class="layui-nav-item ${about!''}"><a href="${contextPath}/about">关于</a></li>
            </ul>
            <@shiro.user>
                <a href="${contextPath}/logout" class="personal pull-left">
                    <i class="layui-icon layui-icon-username"></i>
                </a>
            </@shiro.user>
            <@shiro.guest>
                <a href="${contextPath}/login" class="personal pull-left">
                    <i class="layui-icon layui-icon-username"></i>
                </a>
            </@shiro.guest>
        </div>
        <div class="mobile-nav pull-right" id="mobile-nav">
            <a href="javascript:;">
                <i class="layui-icon layui-icon-more"></i>
            </a>
        </div>
    </div>
    <ul class="pop-nav" id="pop-nav">
        <li><a href="${contextPath}/index">文章</a></li>
        <@shiro.user>
            <li><a href="${contextPath}/create">撰写</a></li>
        </@shiro.user>
        <li><a href="${contextPath}/message">留言</a></li>
        <li><a href="${contextPath}/about">关于</a></li>
        <@shiro.user>
            <li><a href="${contextPath}/logout">退出</a></li>
        </@shiro.user>
        <@shiro.guest>
            <li><a href="${contextPath}/login">登录</a></li>
        </@shiro.guest>
    </ul>
</div>