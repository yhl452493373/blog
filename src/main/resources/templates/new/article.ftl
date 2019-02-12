<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>文章</title>
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
            <h1>个人博客文章</h1>
            <p>个人博客文章，记录一些优秀个人站长是如何制作个人博客，建个人博客、以及经营个人网站的，本站还会推荐一些优秀的个人博客站长网站。</p>
        </div>
        <div class="whitebg bloglist">
            <h2 class="htitle">全部博文</h2>

            <div id="itemList"></div>

            <!--pagelist-->
            <div class="list-page" id="pages"></div>
            <!--pagelist end-->
        </div>

        <!--bloglist end-->
    </div>
    <div class="rbox">
        <#include "include.sidebar.ftl">
    </div>
</article>
<#include "include.footer.ftl">
<script type="text/html" id="itemListTemplate">
    {{# if ( d.itemList && d.itemList.length == 0 ) { }}
        <p class="empty">
            暂无数据
        </p>
    {{# } else { }}
        <ul>
            {{# layui.each(d.itemList,function(index,article){ }}
                {{# var imageIds=[]; }}
                {{# if ( article.hasOwnProperty('imageFileIds') && article.imageFileIds.trim() !=='' ) { }}
                    {{# imageIds=article.imageFileIds.split(","); }}
                {{# } }}
                {{# if ( imageIds.length==0 ) { }}
                    <!--没有图片id,则为纯文字模式-->
                    <li>
                        <h3 class="blogtitle">
                            <a href="/detail/{{ article.id }}" target="_blank">{{ article.title }}</a>
                        </h3>
                        <div class="blogtext">{{ article.summary }}</div>
                        <p class="bloginfo">
                            <i class="avatar">
                                <img src="${contextPath}/static/new/images/avatar.png">
                            </i>
                            <span>{{ article.authorName }}</span>
                            <span>{{ article.publishTime }}</span>
                            <#--暂时不展示标签-->
                            <#--<span>【<a href="/">原创模板</a>】</span>-->
                        </p>
                        <a href="/detail/{{ article.id }}" class="viewmore">阅读更多</a>
                    </li>
                {{# } else if ( imageIds.length==1 ) { }}
                    <!-- 图片数量为1个,则为普通单图模式,只显示1张图片 -->
                    <li class="piclist">
                        <h3 class="blogtitle">
                            <a href="/detail/{{ article.id }}" target="_blank">{{ article.title }}</a>
                        </h3>
                        <span class="blogpic imgscale">
                                    <#--暂时不展示标签-->
                            <#--<i><a href="/">原创模板</a></i>-->
                            {{# layui.each(imageIds,function(index,imageId){ }}
                                <a href="/detail/{{ article.id }}" title="">
                                    <img src="https://img.zcool.cn/community/016eae5a26227ca801216e8d48b176.jpg@1280w_1l_2o_100sh.jpg" alt="">
                                </a>
                            {{# }); }}
                        </span>
                        <div class="blogpic-item">
                            <div class="blogtext">{{ article.summary }}</div>
                            <p class="bloginfo">
                                <i class="avatar">
                                    <img src="${contextPath}/static/new/images/avatar.png">
                                </i>
                                <span>{{ article.authorName }}</span>
                                <span>{{ article.publishTime }}</span>
                                <#--暂时不展示标签-->
                                <#--<span>【<a href="/">原创模板</a>】</span>-->
                            </p>
                            <a href="/detail/{{ article.id }}" class="viewmore">阅读更多</a>
                        </div>
                    </li>
                {{# } else { }}
                    <!-- 图片数量大于等于2个,则为多图模式 -->
                    <li class="piclist mutipiclist">
                        <h3 class="blogtitle">
                            <a href="/detail/{{ article.id }}" target="_blank">
                                <#--不置顶-->
                                <#--<b>【顶】</b>-->
                                {{ article.title }}
                            </a>
                        </h3>
                        <span class="bplist">
                            {{# layui.each(imageIds,function(index,imageId){ }}
                                <a href="/detail/{{ article.id }}" title="">
                                    <img src="https://img.zcool.cn/community/016eae5a26227ca801216e8d48b176.jpg@1280w_1l_2o_100sh.jpg" alt="">
                                </a>
                            {{# }); }}
                        </span>
                        <div class="blogtext">{{ article.summary }}</div>
                        <p class="bloginfo">
                            <i class="avatar">
                                <img src="${contextPath}/static/new/images/avatar.png">
                            </i>
                            <span>{{ article.authorName }}</span>
                            <span>{{ article.publishTime }}</span>
                            <#--暂时不展示标签-->
                            <#--<span>【<a href="/">原创模板</a>】</span>-->
                        </p>
                        <a href="/detail/{{ article.id }}" class="viewmore">阅读更多</a>
                    </li>
                {{# } }}
            {{# }); }}
        </ul>
    {{# } }}
</script>
<script>
    layui.use(['laypage', 'layer', 'laytpl'], function () {
        var laypage = layui.laypage, layer = layui.layer, laytpl = layui.laytpl;

        function loadData(size=5, current=1) {
            var loadIndex;
            $.ajax({
                url: 'data/article/list',
                type: 'post',
                data: {
                    isDraft: 0,//查询非草稿文章
                    size: size,
                    current: current
                },
                beforeSend:function(){
                  loadIndex = layer.load();
                },
                success: function (result) {
                    if (result.status === 'success') {
                        renderItemList({itemList: result.data});
                        //渲染一个laypage实例
                        laypage.render({
                            elem: 'pages' //注意，这里的 elem 是 ID，不用加 # 号
                            , limit: size
                            , count: result.count //数据总数，从服务端得到
                            , curr: current
                            , layout: ['count', 'prev', 'page', 'next']
                            , jump: function (obj, first) {
                                //obj包含了当前分页的所有参数，比如：
                                //console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                                //console.log(obj.limit); //得到每页显示的条数
                                //首次不执行
                                if (!first) {
                                    //do something
                                    loadData(obj.limit, obj.curr);
                                }
                            }
                        });
                    }else{
                        layer.alert(result.message);
                    }
                },
                complete:function () {
                    layer.close(loadIndex);
                }
            });
        }

        function renderItemList(itemList) {
            var view = $('#itemListTemplate').html();
            //模板渲染
            laytpl(view).render(itemList, function (html) {
                $('#itemList').html(html);
            });
        }

        loadData();
    });
</script>
</body>
</html>
