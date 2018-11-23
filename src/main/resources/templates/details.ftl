<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文章-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<#include "include.resource.ftl">
    <style>
        .info-item {
            margin-bottom: 0 !important;
            margin-top: 35px !important;
            padding: 10px !important;
            border-bottom: 1px dashed #ddd;
        }

        .info-item.empty {
            line-height: 40px;
            text-align: center;
            padding-bottom: 20px;
        }

        #commentItemList {
            background-color: #fff;
            padding: 10px;
            margin-top: 30px !important;
        }

        .article-count {
            margin-top: 0 !important;
            padding: 0 35px 10px;
        }
    </style>
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container container-message container-details">
        <div class="contar-wrap">
            <div class="item">
                <div class="item-box item-content-box layer-photos-demo layer-photos-demo1">
                    <h3>${article.title}</h3>
                    <h5>发布于：<span>${article.publishTime}</span></h5>
                    <div class="item-content">${article.content}</div>
                </div>
                <div class="count article-count layui-clear">
                    <span class="pull-left">阅读 <em>${article.readCount}</em></span>
                    <span class="pull-right like" data-id="${article.id}">
                        <i class="layui-icon layui-icon-praise"></i><em class="count">${article.praiseCount}</em>
                    </span>
                </div>
            </div>
            <div class="comt layui-clear" style="margin-bottom: 0">
                <a href="javascript:;" class="pull-left">评论</a>
                <a href="${contextPath}/comment/${article.id}" class="pull-right">写评论</a>
            </div>
            <div id="commentItemList"></div>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script type="text/html" id="commentItem">
    <div class="info-item">
    <#--头像-->
    <#--<img class="info-img" src="../res/static/images/info-img.png" alt="">-->
        <div class="info-text" style="padding-left: 0">
            <p class="title count" style="margin-top: 0">
                <span class="name">{{ d.userName }} 于 {{ d.createdTime }} 评论:</span>
                <span class="info-img like" data-id="{{ d.id }}"><i class="layui-icon layui-icon-praise"></i><span
                        class="count">{{ d.praiseCount }}</span></span>
            </p>
            <p class="info-intr">{{ d.content }}</p>
        </div>
    </div>
</script>
<script type="text/html" id="commentItemEmpty">
    <div class="info-item empty">
        {{ d.message }}
    </div>
</script>
<script>
    var articleId = '${article.id}';

    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog'
    }).use(['blog', 'jquery', 'laytpl'], function () {
        var $ = layui.jquery, laytpl = layui.laytpl;
        var blog = layui.blog;

        function loadComment() {
            $.ajax({
                url: contextPath + '/data/comment/list',
                type: 'post',
                data: {
                    articleId: articleId
                },
                success: function (result) {
                    if (result.status === 'success') {
                        if (result.data.length > 0) {
                            result.data.forEach(function (item) {
                                renderData(item);
                            });
                        } else {
                            renderEmpty({
                                message: '还没有人评论,来抢沙发!'
                            });
                        }
                    }
                }
            })
        }

        function renderData(item) {
            var view = $('#commentItem').html();
            //模板渲染
            laytpl(view).render(item, function (html) {
                $('#commentItemList').children('.item-info.empty').remove().end().append(html);
            });
        }

        function renderEmpty(item) {
            var $items = $('#commentItemList').children('.item-info');
            if ($items.length === 0) {
                var view = $('#commentItemEmpty').html();
                //模板渲染
                laytpl(view).render(item, function (html) {
                    $('#commentItemList').append(html);
                });
            }
        }

        loadComment();

        blog.praise('.pull-right', blog.praise.paramType.articleId);
        blog.praise('.info-img', blog.praise.paramType.commentId);
    });
</script>
</body>
</html>