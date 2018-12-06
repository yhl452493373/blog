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
                <div class="item-box item-content-box layer-photos-demo layer-photos-demo1" style="padding-bottom: 20px">
                    <h3>
                        <@shiro.user>
                            <a class="layui-icon layui-icon-edit" href="${contextPath}/edit/{{ d.id }}"></a>
                        </@shiro.user>
                        ${article.title}
                    </h3>
                    <h5>发布于：<span>${article.publishTime?string('yyyy-MM-dd HH:mm:ss')}</span></h5>
                    <div class="item-content">${article.content}</div>
                </div>
            </div>
            <form id="commentForm" class="layui-form" action="${contextPath}/data/comment/add" method="post">
                <input type="hidden" name="articleId" value="${article.id}">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-input-inline">
                            <input type="text" name="userName" value="<@shiro.principal property='username'/>"
                                   placeholder="您的昵称" class="layui-input">
                        </div>
                    </div>
                </div>

                <div class="layui-form-item layui-form-text">
                    <textarea name="content" class="layui-textarea" style="resize:none" placeholder="写点什么啊"></textarea>
                </div>
                <div class="btnbox">
                    <a id="comment" class="layui-btn layui-btn-normal">写好了</a>
                </div>
            </form>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
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
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog'
    }).use(['blog', 'layer', 'jquery'], function () {
        var $ = layui.jquery, layer = layui.layer;

        $(document).on('click', '#comment', function (e) {
            e.preventDefault();
            comment();
        });

        function comment() {
            var $commentForm = $('#commentForm');
            var formData = new FormData($commentForm.get(0));
            if(formData.get("userName").trim().length===0){
                layer.msg('昵称不能为空');
                return;
            }else if(formData.get("content").trim().length===0){
                layer.msg('评论内容不能为空');
                return;
            }
            $.ajax({
                url: $commentForm.attr('action'),
                type: $commentForm.attr('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.status === 'success') {
                        layer.alert('评论成功!', function (index) {
                            layer.close(index);
                            window.location.href = contextPath + '/details/' + result.data['articleId'];
                        });
                    } else {
                        layer.alert(result.message);
                    }
                }
            });
        }
    });
</script>
</body>
</html>