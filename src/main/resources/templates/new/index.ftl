<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>首页</title>
    <#include "include.resource.ftl">
</head>
<body>
<!--top begin-->
<#include "include.header.ftl">
<!--top end-->
<article>
    <!--lbox begin-->
    <div class="lbox">
        <#--<!--banbox begin&ndash;&gt;-->
        <#--&lt;#&ndash;最新图片&ndash;&gt;-->
        <#--<div class="banbox">-->
        <#--<div class="banner">-->
        <#--<div id="banner" class="fader">-->
        <#--<ul>-->
        <#--<li class="slide"><a href="/" target="_blank"><img src="images/1.jpg"></a></li>-->
        <#--<li class="slide"><a href="/" target="_blank"><img src="images/2.jpg"></a></li>-->
        <#--<li class="slide"><a href="/" target="_blank"><img src="images/3.jpg"></a></li>-->
        <#--<li class="slide"><a href="/" target="_blank"><img src="images/4.jpg"></a></li>-->
        <#--</ul>-->
        <#--<div class="fader_controls">-->
        <#--<div class="page prev" data-target="prev"></div>-->
        <#--<div class="page next" data-target="next"></div>-->
        <#--<ul class="pager_list">-->
        <#--</ul>-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->
        <#--</div>-->
        <#--<!--banbox end&ndash;&gt;-->
        <#--<!--headline begin&ndash;&gt;-->
        <#--&lt;#&ndash;最热门相册&ndash;&gt;-->
        <#--<div class="headline">-->
        <#--<ul>-->
        <#--<li><a href="/" title="为什么说10月24日是程序员的节日？"><img src="images/h1.jpg" alt="为什么说10月24日是程序员的节日？"><span>为什么说10月24日是程序员的节日？</span></a></li>-->
        <#--<li><a href="/" title="个人网站做好了，百度不收录怎么办？来，看看他们怎么做的"><img src="images/h2.jpg" alt="个人网站做好了，百度不收录怎么办？来，看看他们怎么做的。"><span>个人网站做好了，百度不收录怎么办？来，看看他们怎么做的。</span></a></li>-->
        <#--</ul>-->
        <#--</div>-->
        <#--<!--headline end&ndash;&gt;-->
        <#--<div class="clearblank"></div>-->
        <!--tab_box begin-->
        <#--标签下的文章-->
        <#if mostTags?has_content>
            <div class="tab_box whitebg">
                <div class="tab_buttons">
                    <ul>
                        <#list mostTags as tag>
                            <#if tag_index==0>
                                <li class="newscurrent">${tag.name}</li>
                            <#else>
                                <li>${tag.name}</li>
                            </#if>
                        </#list>
                    </ul>
                </div>
                <div class="newstab">
                    <#list mostTags as tag>
                        <div class="newsitem">
                            <#assign pics="">
                            <#assign articleA="">
                            <#assign articleB="">
                            <#list tag.articleList as article>
                                <#assign tempPic=article.imageFileIds!?split(",")[0]>
                                <#if tempPic?has_content>
                                    <#if (article_index gt 0) && (article_index lt 2)>
                                        <#assign pics+=",">
                                    </#if>
                                    <#if pics?split(",")?size==1>
                                        <#assign articleA=article>
                                    <#elseif pics?split(",")?size==2>
                                        <#assign articleB=article>
                                    </#if>
                                    <#assign pics=pics+tempPic>
                                </#if>
                            </#list>
                            <#if pics?has_content>
                                <#assign pics=pics?split(",")>
                            <#else>
                                <#assign pics=[]>
                            </#if>
                            <#if pics?size gt 0>
                                <div class="newspic">
                                    <ul>
                                        <#list pics as picId>
                                            <#if picId_index==0>
                                                <li><img src="${contextPath}/data/file/download/${picId}"><span>${articleA.title}</span></li>
                                            <#elseif picId_index==1>
                                                <li><img src="${contextPath}/data/file/download/${picId}"><span>${articleB.title}</span></li>
                                            </#if>
                                        </#list>
                                    </ul>
                                </div>
                            </#if>
                            <ul class="newslist">
                                <#list tag.articleList as article>
                                    <li>
                                        <i></i><span>${article.title}</span>
                                        <p><a href="/">${article.summary}</a></p>
                                    </li>
                                </#list>
                            </ul>
                        </div>
                    </#list>
                </div>
            </div>
        </#if>
        <!--tab_box end-->
        <div class="whitebg bloglist">
            <h2 class="htitle">最新博文</h2>
            <#if !newest?has_content>
                <p class="empty">
                    暂无数据
                </p>
            <#else>
                <ul>
                    <#list newest as article>
                        <#if article.imageFileIds??>
                            <#assign imageIds=article.imageFileIds!?split(",")>
                        <#else>
                            <#assign imageIds=[]>
                        </#if>
                        <#if !imageIds?has_content>
                            <!--没有图片id,则为纯文字模式-->
                            <li>
                                <h3 class="blogtitle">
                                    <a href="/" target="_blank">${article.title}</a>
                                </h3>
                                <p class="blogtext">${article.summary}</p>
                                <p class="bloginfo">
                                    <i class="avatar">
                                        <img src="${contextPath}/static/new/images/avatar.png">
                                    </i>
                                    <span>${article.authorName}</span>
                                    <span>${article.publishTime?datetime}</span>
                                    <#--暂时不展示标签-->
                                    <#--<span>【<a href="/">原创模板</a>】</span>-->
                                </p>
                                <a href="/" class="viewmore">阅读更多</a>
                            </li>
                        <#elseif imageIds?size == 1>
                            <!-- 图片数量小于1个,则为普通单图模式,只显示1张图片 -->
                            <li class="piclist">
                                <h3 class="blogtitle">
                                    <a href="/" target="_blank">${article.title}</a>
                                </h3>
                                <span class="blogpic imgscale">
                                    <#--暂时不展示标签-->
                                    <#--<i><a href="/">原创模板</a></i>-->
                                    <#list imageIds as imageId>
                                        <a href="/" title="">
                                            <img src="https://img.zcool.cn/community/016eae5a26227ca801216e8d48b176.jpg@1280w_1l_2o_100sh.jpg" alt="">
                                        </a>
                                    </#list>
                                </span>
                                <div class="blogpic-item">
                                    <p class="blogtext">${article.summary}</p>
                                    <p class="bloginfo">
                                        <i class="avatar">
                                            <img src="${contextPath}/static/new/images/avatar.png">
                                        </i>
                                        <span>${article.authorName}</span>
                                        <span>${article.publishTime?datetime}</span>
                                        <#--暂时不展示标签-->
                                        <#--<span>【<a href="/">原创模板</a>】</span>-->
                                    </p>
                                    <a href="/" class="viewmore">阅读更多</a>
                                </div>
                            </li>
                        <#else>
                            <!-- 图片数量大于等于1个,则为多图模式 -->
                            <li class="piclist mutipiclist">
                                <h3 class="blogtitle">
                                    <a href="/" target="_blank">
                                        <#--不置顶-->
                                        <#--<b>【顶】</b>-->
                                        ${article.title}
                                    </a>
                                </h3>
                                <span class="bplist">
                                    <#list imageIds as imageId>
                                        <a href="/">
                                            <img src="https://img.zcool.cn/community/01875f5b971a35a80121a0f7579922.jpg@1280w_1l_0o_100sh.jpg" alt="">
                                        </a>
                                    </#list>
                                </span>
                                <p class="blogtext">${article.summary}</p>
                                <p class="bloginfo">
                                    <i class="avatar">
                                        <img src="${contextPath}/static/new/images/avatar.png">
                                    </i>
                                    <span>${article.authorName}</span>
                                    <span>${article.publishTime?datetime}</span>
                                    <#--暂时不展示标签-->
                                    <#--<span>【<a href="/">原创模板</a>】</span>-->
                                </p>
                                <a href="/" class="viewmore">阅读更多</a>
                            </li>
                        </#if>
                    </#list>
                </ul>
            </#if>
        </div>
        <!--bloglist end-->
    </div>
    <div class="rbox">
        <div class="card">
            <h2>我的名片</h2>
            <p>网名：Isve丨丶勿言 | yhl452493373</p>
            <p>职业：程序猿</p>
            <p>现居：四川省-成都市</p>
            <p>Email：yanghuanglin@qq.com</p>
            <ul class="linkmore">
                <li><a href="http://www.yanghuanglin.com" target="_blank" title="网站地址"><i class="blog-icon blog-icon-home"></i></a></li>
                <li><a href="mailto://yanghuanglin@qq.com" target="_blank" title="我的邮箱"><i class="blog-icon blog-icon-email"></i></a></li>
                <li><a href="http://wpa.qq.com/msgrd?v=3&uin=452493373&site=qq&menu=yes" target="_blank" title="QQ联系我"><i class="blog-icon blog-icon-qq"></i></a></li>
                <li><a href="https://github.com/yhl452493373" target="_blank" title="GitHub"><i class="blog-icon blog-icon-github"></i></a></li>
            </ul>
        </div>
        <#include "include.sidebar.ftl">
    </div>
</article>
<#include "include.footer.ftl">
</body>
</html>
