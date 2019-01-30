<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>相册</title>
    <link rel="stylesheet" href="${contextPath}/static/lib/layui/css/layui.css">
    <script src="${contextPath}/static/lib/layui/layui.js"></script>
    <#include "include.resource.ftl">
</head>
<body>
<!--top begin-->
<#include "include.header.ftl">
<!--top end-->
<article>
    <!--lbox begin-->
    <div class="lbox">
        <div class="whitebg lanmu"><img src="images/lm01.jpg">
            <h1>个人博客相册</h1>
            <p>个人博客相册，记录个人的生活点滴。</p>
        </div>

        <div class="banbox">
            <div class="banner">
                <div id="banner" class="fader">
                    <ul>
                        <li class="slide"><a href="/" target="_blank"><img src="images/1.jpg"></a></li>
                        <li class="slide"><a href="/" target="_blank"><img src="images/2.jpg"></a></li>
                        <li class="slide"><a href="/" target="_blank"><img src="images/3.jpg"></a></li>
                        <li class="slide"><a href="/" target="_blank"><img src="images/4.jpg"></a></li>
                    </ul>
                    <div class="fader_controls">
                        <div class="page prev" data-target="prev"></div>
                        <div class="page next" data-target="next"></div>
                        <ul class="pager_list">
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <!--banbox end-->

        <!--headline begin-->
        <div class="headline">
            <ul>
                <li><a href="/" title="为什么说10月24日是程序员的节日？"><img src="images/h1.jpg" alt="为什么说10月24日是程序员的节日？"><span>为什么说10月24日是程序员的节日？</span></a></li>
                <li><a href="/" title="个人网站做好了，百度不收录怎么办？来，看看他们怎么做的"><img src="images/h2.jpg" alt="个人网站做好了，百度不收录怎么办？来，看看他们怎么做的。"><span>个人网站做好了，百度不收录怎么办？来，看看他们怎么做的。</span></a></li>
            </ul>
        </div>
        <!--headline end-->

        <div class="clearblank"></div>

        <!--zhuanti begin-->
        <div class="zhuanti whitebg">
            <h2 class="htitle"><span class="hnav"></span>最近更新</h2>
            <ul>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/1.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/2.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/3.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
            </ul>
        </div>
        <!--zhuanti end-->

        <!--zhuanti begin-->
        <div class="zhuanti whitebg">
            <h2 class="htitle"><span class="hnav"></span>所有相册</h2>
            <ul>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/1.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/2.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/3.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/4.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/h2.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/h1.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/1.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/2.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
                <li><i class="ztpic"><a href="/" target="_blank"><img src="images/3.jpg"></a></i> <b>个人博客模板《今夕何夕》-响应式个人...</b><span>个人博客模板《今夕何夕》，宽屏响应式个人博客模板，采用冷色系为主，固定导航栏和侧边栏，无缝滚动图片...</span><a href="" target="_blank" class="readmore">文章阅读</a></li>
            </ul>

            <!--pagelist-->
            <div class="list-page" id="pages"></div>
            <!--pagelist end-->

        </div>
        <!--zhuanti end-->
    </div>
    <div class="rbox">
        <#include "include.sidebar.ftl">
    </div>
</article>
<#include "include.footer.ftl">
<script>
    layui.use(['laypage'], function () {
        var laypage = layui.laypage;

        //执行一个laypage实例
        laypage.render({
            elem: 'pages' //注意，这里的 pages 是 ID，不用加 # 号
            , count: 50 //数据总数，从服务端得到
            , groups: 3 //连续出现的页码个数
            , jump: function (obj, first) {
                //obj包含了当前分页的所有参数，比如：
                console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                console.log(obj.limit); //得到每页显示的条数

                //首次不执行
                if (!first) {
                    //do something
                }
            }
        });
    });
</script>
</body>
</html>
