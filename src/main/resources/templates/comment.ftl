<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>评论-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<#include "include.resource.ftl">
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container container-message container-details container-comment">
        <div class="contar-wrap">
            <div class="item">
                <div class="item-box item-content-box">
                    <h3>${article.title}</h3>
                    <h5>发布于：<span>${article.publishTime}</span></h5>
                    <div class="item-content">${article.content}</div>
                </div>
            </div>
            <form class="layui-form" action="">
                <div class="layui-form-item layui-form-text">
                    <textarea class="layui-textarea" style="resize:none" placeholder="写点什么啊"></textarea>
                </div>
                <div class="btnbox">
                    <a href="${contextPath}/details/${article.id}" id="sure" class="layui-btn layui-btn-normal">
                        确定
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.extend({
        blog: '{/}../res/layui-ext/blog/blog'
    }).use('blog');
</script>
<script id="LAY-msg-tpl" type="text/html">
    <div class="info-box">
        <div class="info-item">
            <img class="info-img" src="{{ d.avatar }}" alt="">
            <div class="info-text">
                <p class="title">
                    <span class="name">{{ d.username }}</span>
                    <span class="info-img">
					  	<i class="layui-icon layui-icon-praise"></i>
					  	{{ d.praise }}
					 	</span>
                </p>
                <p class="info-intr">
                    {{ d.content }}
                </p>
            </div>
        </div>
    </div>
</script>
</body>
</html>