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
                    <h3>
                        <@shiro.user>
                            <a class="layui-icon layui-icon-edit" href="${contextPath}/edit/${article.id}"></a>
                            <a class="layui-icon layui-icon-delete article-delete" href="${contextPath}/data/article/delete?id=${article.id}"></a>
                        </@shiro.user>
                        ${article.title}
                    </h3>
                    <h5>发布于：<span>${article.publishTime?string('yyyy-MM-dd HH:mm:ss')}</span></h5>
                    <h6 style="font-size: 12px">
                        <#list tagList as tag>
                            <a href="#" title="${tag.name}相关文章" style="color: #999">${tag.name}</a>
                        </#list>
                    </h6>
                    <div class="item-content fr-view">${article.content}</div>
                </div>
                <div class="count article-count layui-clear">
                    <span class="pull-left">阅读 <em>${article.readCount}</em></span>
                    <span class="pull-right like" data-id="${article.id}" id="praiseCount">
                        <i class="layui-icon layui-icon-praise"></i><em class="count">${article.praiseCount}</em>
                    </span>
                </div>
            </div>
            <div class="comt layui-clear" style="margin-bottom: 0">
                <a href="javascript:;" class="pull-left">评论</a>
                <a href="${contextPath}/comment/${article.id}" class="pull-right">写评论</a>
            </div>
            <div id="commentItemList"></div>
            <div id="page"></div>
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
                <span class="name">
                    <@shiro.user>
                        <a class="layui-icon layui-icon-delete comment-delete" href="${contextPath}/data/comment/delete?id={{ d.id }}"></a>
                    </@shiro.user>
                    第{{ d.floor }}楼. {{ d.userName }} 于 {{ d.createdTime }} 评论:
                </span>
                <span class="info-img like {{# if (d.praised) { }}layblog-this{{# } }}" data-id="{{ d.id }}">
                    <i class="layui-icon layui-icon-praise"></i>
                    <span class="count">{{ d.praiseCount }}</span>
                </span>
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
    }).use(['blog', 'jquery', 'laytpl', 'laypage'], function () {
        var $ = layui.jquery, laytpl = layui.laytpl, laypage = layui.laypage;
        var blog = layui.blog;

        $(document).on('click.article-delete', '.article-delete', function (e) {
            e.preventDefault();
            deleteArticle.call(this);
        }).on('click.comment-delete', '.comment-delete', function (e) {
            e.preventDefault();
            deleteComment.call(this);
        });

        function deleteComment() {
            var href = this.getAttribute('href');
            layer.confirm('确定删除这条评论?', function (index) {
                $.ajax({
                    url: href,
                    success: function (result) {
                        if (result.status === 'success') {
                            layer.alert('删除成功', function (index) {
                                loadComment();
                                layer.close(index);
                            })
                        } else {
                            layer.alert(result.message);
                        }
                    }
                });
                layer.close(index);
            });
        }

        function deleteArticle() {
            var href = this.getAttribute('href');
            layer.confirm('确定删除这篇文章?', function (index) {
                $.ajax({
                    url: href,
                    success: function (result) {
                        if (result.status === 'success') {
                            layer.alert('删除成功', function () {
                                window.location.href = contextPath + "/index";
                            })
                        } else {
                            layer.alert(result.message);
                        }
                    }
                });
                layer.close(index);
            });
        }

        /**
         * 加载评论列表
         * @param size 每页条数
         * @param current 当前页数
         */
        function loadComment(size=10, current=1) {
            $.ajax({
                url: contextPath + '/data/comment/list',
                type: 'post',
                data: {
                    articleId: articleId,
                    size: size,
                    current: current
                },
                success: function (result) {
                    if (result.status === 'success') {
                        //渲染一个laypage实例
                        laypage.render({
                            elem: 'page' //注意，这里的 elem 是 ID，不用加 # 号
                            , limit: size
                            , count: result.count //数据总数，从服务端得到
                            , curr: current
                            , layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip']
                            , jump: function (obj, first) {
                                //obj包含了当前分页的所有参数，比如：
                                //console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                                //console.log(obj.limit); //得到每页显示的条数
                                //首次不执行
                                if (!first) {
                                    //do something
                                    loadComment(obj.limit, obj.curr);
                                }
                            }
                        });
                        $('#commentItemList > .info-item').remove();
                        if (result.data.length > 0) {
                            result.data.forEach(function (item) {
                                if (localStorage.getItem(item.id + '_praised') === 'yes') {
                                    item.praised = true;
                                }
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

        function initPraised() {
            var $praiseCount = $('#praiseCount');
            var itemId = $praiseCount.data('id');
            if (localStorage.getItem(itemId + '_praised') === 'yes') {
                $praiseCount.addClass('layblog-this');
            }
        }

        function increaseReadCount() {
            var $praiseCount = $('#praiseCount');
            if (localStorage.getItem($praiseCount.data('id') + '_read') !== 'yes') {
                $.get(contextPath + '/data/article/increaseReadCount?id=' + $praiseCount.data('id'), function (result) {
                    console.log(result.status);
                    if (result.status === 'success') {
                        localStorage.setItem($praiseCount.data('id') + '_read', 'yes');
                    }
                });
            }
        }

        increaseReadCount();
        initPraised();
        loadComment();

        blog.praise('.pull-right', blog.praise.paramType.article);
        blog.praise('.info-img', blog.praise.paramType.comment);
    });
</script>
</body>
</html>