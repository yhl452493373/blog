<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文章-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<#include "include.resource.ftl">
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container container-message container-details">
        <div class="contar-wrap">
            <div class="item">
                <div class="item-box item-content-box">
                    <h3>${article.title}</h3>
                    <h5>发布于：<span>${article.publishTime}</span></h5>
                    <div class="item-content">${article.content}</div>
                </div>
            </div>
            <a name="comment"> </a>
            <div class="comt layui-clear">
                <a href="javascript:;" class="pull-left">评论</a>
                <a href="${contextPath}/comment/${article.id}" class="pull-right">写评论</a>
            </div>
            <div id="LAY-msg-box">
                <div class="info-item">
                    <img class="info-img" src="../res/static/images/info-img.png" alt="">
                    <div class="info-text">
                        <p class="title count">
                            <span class="name">一片空白</span>
                            <span class="info-img like"><i class="layui-icon layui-icon-praise"></i>5.8万</span>
                        </p>
                        <p class="info-intr">
                            父爱如山，不善表达。回想十多年前，总记得父亲有个宽厚的肩膀，小小的自己跨坐在上面，越过人山人海去看更广阔的天空，那个时候期望自己有一双翅膀，能够像鸟儿一样飞得高，看得远。虽然父亲有时会和自己开玩笑，但在做错事的时候会受到严厉的训斥。父亲有双粗糙的大手掌。</p>
                    </div>
                </div>
                <div class="info-item">
                    <img class="info-img" src="../res/static/images/info-img.png" alt="">
                    <div class="info-text">
                        <p class="title count">
                            <span class="name">一片空白</span>
                            <span class="info-img like"><i class="layui-icon layui-icon-praise"></i>5.8万</span>
                        </p>
                        <p class="info-intr">
                            父爱如山，不善表达。回想十多年前，总记得父亲有个宽厚的肩膀，小小的自己跨坐在上面，越过人山人海去看更广阔的天空，那个时候期望自己有一双翅膀，能够像鸟儿一样飞得高，看得远。虽然父亲有时会和自己开玩笑，但在做错事的时候会受到严厉的训斥。父亲有双粗糙的大手掌。</p>
                    </div>
                </div>
            </div>
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