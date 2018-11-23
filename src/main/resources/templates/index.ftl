<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<#include "include.resource.ftl">
    <style>
        .item.empty {
            line-height: 40px;
            text-align: center;
            padding-bottom: 20px;
        }
    </style>
</head>
<body class="lay-blog">
		<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container">
        <div id="articleItemList" class="contar-wrap">
            <h4 class="item-title">
                <p><i class="layui-icon layui-icon-speaker"></i>公告：<span>欢迎来到我的轻博客</span></p>
            </h4>
        </div>
        <div class="item-btn">
            <button class="layui-btn layui-btn-normal">下一页</button>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script type="text/html" id="articleItem">
    <div class="item">
        <div class="item-box layer-photos-demo layer-photos-demo{{ d.index }}">
            <h3><a href="${contextPath}/details/{{ d.id }}">{{ d.title }}</a></h3>
            <h5>发布于：<span>{{ d.publishTime }}</span></h5>
            <div class="preview-content">{{ d.content }}</div>
        </div>
        <div class="comment count">
            <a href="${contextPath}/comment/{{ d.id }}">评论</a>
            <a href="javascript:void(0);" class="like" data-id="{{ d.id }}">点赞</a>
        </div>
    </div>
</script>
<script type="text/html" id="articleItemEmpty">
    <div class="item empty">
        {{ d.message }}
    </div>
</script>
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog'
    }).use(['blog', 'jquery', 'layer', 'laytpl'], function () {
        var $ = layui.jquery, layer = layui.layer, laytpl = layui.laytpl;
        var blog = layui.blog;

        function loadArticle() {
            $.ajax({
                url: contextPath + '/data/article/list',
                type: 'post',
                data: {
                    isDraft: 0//查询非草稿文章
                },
                success: function (result) {
                    if (result.status === 'success') {
                        if (result.data.length > 0) {
                            result.data.forEach(function (item, index) {
                                item.index = index + 1;
                                renderData(item);
                            });
                        } else {
                            renderEmpty({
                                message: '此处空空如也。。。。'
                            });
                        }
                    }
                }
            })
        }

        function renderData(item) {
            var view = $('#articleItem').html();
            //模板渲染
            laytpl(view).render(item, function (html) {
                $('#articleItemList').children('.item.empty').remove().end().append(html);
                blog.initLayerPhotos('.layer-photos-demo' + item.index);
            });
        }

        function renderEmpty(item) {
            var $items = $('#articleItemList').children('.item');
            if ($items.length === 0) {
                var view = $('#articleItemEmpty').html();
                //模板渲染
                laytpl(view).render(item, function (html) {
                    $('#articleItemList').append(html);
                });
            }
        }

        loadArticle();
        blog.praise('', blog.praise.paramType.articleId);
    });
</script>
</body>
</html>