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
                    <input type="text" name="title" lay-verify="title" autocomplete="off"  class="layui-input">
                </div>
            </div>
        </form>
        <div class="blog-nav pull-right">
            <ul class="layui-nav pull-left">
                <li class="layui-nav-item layui-this"><a href="index.html">文章</a></li>
                <li class="layui-nav-item"><a href="create.html">撰写</a></li>
                <li class="layui-nav-item"><a href="message.html">留言</a></li>
                <li class="layui-nav-item"><a href="about.html">关于</a></li>
            </ul>
            <a href="login.html" class="personal pull-left">
                <i class="layui-icon layui-icon-username"></i>
            </a>
        </div>
        <div class="mobile-nav pull-right" id="mobile-nav">
            <a href="javascript:;">
                <i class="layui-icon layui-icon-more"></i>
            </a>
        </div>
    </div>
    <ul class="pop-nav" id="pop-nav">
        <li><a href="index.html">文章</a></li>
        <li><a href="create.html">撰写</a></li>
        <li><a href="message.html">留言</a></li>
        <li><a href="about.html">关于</a></li>
        <li><a href="login.html">登录</a></li>
    </ul>
</div>