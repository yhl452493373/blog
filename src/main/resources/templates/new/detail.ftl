<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>文章内容</title>
    <#include "include.resource.ftl">
</head>
<body>
<!--top begin-->
<#include "include.header.ftl">
<!--top end-->
<article>
    <!--lbox begin-->
    <div class="lbox">
        <div class="content_box whitebg">
            <h2 class="htitle">文章内容</h2>
            <h1 class="con_tilte">${detail.title}</h1>
            <p class="bloginfo" style="position: static">
                <i class="avatar">
                    <img src="${contextPath}/static/new/images/avatar.png">
                </i>
                <span>${detail.authorName}</span>
                <span>${detail.createdTime?datetime}</span>
                <span>${detail.readCount}人已围观</span>
            </p>
            <#--<p class="con_info"><b>简介</b>个人博客，用来做什么？我刚开始就把它当做一个我吐槽心情的地方，也就相当于一个网络记事本，写上一些关于自己生活工作中的小情小事，也会放上一些照片，音乐。每天工作回家后就能访问自己的网站，一边听着音乐，一边写写文章。</p>-->
            <div class="con_text">
                <div>
                    ${detail.content}
                </div>
                <div class="nextinfo">
                    <p>上一篇：<a href="/download/f/886.html">html5 个人博客模板《蓝色畅想》</a></p>
                    <p>下一篇：<a href="/download/f/907.html">个人博客模板《tree》-响应式个人网站模板</a></p>
                </div>
            </div>
        </div>

        <div class="whitebg gbook">
            <h2 class="htitle">文章评论</h2>
            <ul>
            </ul>
        </div>
    </div>
    <!--lbox end-->
    <div class="rbox">
        <#include "include.sidebar.ftl">
    </div>
</article>
<#include "include.footer.ftl">
</body>
</html>
