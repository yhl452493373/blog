<#include "include.common.ftl">
<header>
    <div class="navbox">
        <h2 id="mnavh"><span class="navicon"></span></h2>
        <div class="logo"><a href="http://www.yanghuanglin.com">个人博客</a></div>
        <nav>
            <ul id="starlist">
                <li class="${index!}"><a href="${contextPath}/index">首页</a></li>
                <li class="${article!}"><a href="${contextPath}/article">文章</a></li>
                <li class="${album!}"><a href="${contextPath}/album">相册</a></li>
                <li class="${message!}"><a href="${contextPath}/message">留言</a></li>
                <li class="${about!}"><a href="${contextPath}/about">关于</a></li>
            </ul>
        </nav>
        <div class="searchico"><i class="blog-icon blog-icon-search"></i></div>
    </div>
</header>
<div class="searchbox">
    <div class="search">
        <form action="data/article/search" method="post" name="searchform" id="searchform">
            <input name="keyboard" id="keyboard" class="input_text" value="请输入关键字词" style="color: rgb(153, 153, 153);" onfocus="if(value=='请输入关键字词'){this.style.color='#000';value=''}" onblur="if(value==''){this.style.color='#999';value='请输入关键字词'}" type="text">
            <input name="show" value="title" type="hidden">
            <input name="tempid" value="1" type="hidden">
            <input name="tbname" value="news" type="hidden">
            <input name="Submit" class="input_submit" value="搜索" type="submit">
        </form>
    </div>
    <div class="searchclose"><i class="blog-icon blog-icon-close"></i></div>
</div>