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
            <div id="articleItemList" class="contar-wrap"></div>
            <div id="page"></div>
        </div>
    </div>
<#include "include.footer.ftl">
<script type="text/html" id="editAnnouncementPopupContent">
    <form id="announcementForm" action="${contextPath}/data/announcement/add" method="post" class="layui-form layui-form-pane">
        <div class="layui-form-item layui-form-text" style="margin: 15px 15px 0 15px">
            <div class="layui-input-block">
                <textarea placeholder="请输入公告内容" class="layui-textarea" name="content" style="height: 220px;max-height: 235px"></textarea>
            </div>
        </div>
    </form>
</script>
<script type="text/html" id="announcement">
    <h4 class="item-title announcement" style="word-break: break-all;line-height: normal;padding: 15px 35px;">
        <p style="padding-left: 0;line-height: 30px">
            {{# if ( d.empty ) { }}
            <a href="javascript:void(0)" style="display: block;text-align: center" class="empty-announcement" id="editAnnouncement">编辑公告</a>
            {{# } else { }}
            <@shiro.user>
                <a class="layui-icon layui-icon-edit" href="javascript:void(0);" id="editAnnouncement"></a>
            </@shiro.user>
            <i class="layui-icon layui-icon-speaker"></i>
            <span>公告：</span>
            <span>{{ d.content }}</span>
            <br>
            <span style="color: #aaa;font-size: 14px;display: block;text-align: right">{{ d.createdTime }}</span>
            {{# } }}
        </p>
    </h4>
</script>
<script type="text/html" id="articleItem">
    <div class="item">
        <div class="item-box layer-photos-demo layer-photos-demo{{ d.index }}">
            <h3>
                <@shiro.user>
                    <a class="layui-icon layui-icon-edit" href="${contextPath}/edit/{{ d.id }}"></a>
                </@shiro.user>
                <a href="${contextPath}/details/{{ d.id }}">{{ d.title }}</a>
            </h3>
            <h5>发布于：<span>{{ d.publishTime }}</span></h5>
            <div class="preview-content">{{ d.summary }}</div>
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
    }).use(['blog', 'jquery', 'layer', 'laytpl', 'laypage'], function () {
        var $ = layui.jquery, layer = layui.layer, laytpl = layui.laytpl, laypage = layui.laypage;
        var blog = layui.blog, logined = <@shiro.user>true</@shiro.user><@shiro.guest>false</@shiro.guest>;

        $(document).on('click.announcement', '#editAnnouncement', function (e) {
            e.preventDefault();
            showEditAnnouncementPopup();
        });

        function showEditAnnouncementPopup() {
            var view = $('#editAnnouncementPopupContent').html();
            laytpl(view).render({}, function (html) {
                layer.open({
                    type: 1
                    , title: '编辑公告'
                    , content: html
                    , area: function () {
                        if (/mobile/i.test(navigator.userAgent) || $(window).width() < 500) {
                            return ['85%', '335px']
                        } else {
                            return ['500px', '335px'];
                        }
                    }()
                    , btn: ['更新', '取消']
                    , btnAlign: 'c'
                    , yes: function (index1, layero) {
                        var $announcementForm = $('#announcementForm');
                        $.ajax({
                            url: $announcementForm.attr('action'),
                            type: $announcementForm.attr('method'),
                            data: $announcementForm.serialize(),
                            success: function (result) {
                                if (result.status === 'success') {
                                    layer.alert('公告编辑成功', function (index2) {
                                        renderAnnouncement(result.data);
                                        layer.close(index2);
                                        layer.close(index1);
                                    });
                                } else {
                                    layer.alert(result.message);
                                }
                            }
                        })
                    }
                });
            });

        }

        function loadAnnouncement() {
            $.ajax({
                url: contextPath + '/data/announcement/newest',
                type: 'post',
                success: function (result) {
                    if (result.status === 'success') {
                        if (result.count === 0)
                            result.data.empty = true;
                        if (!result.data.empty || logined)
                            renderAnnouncement(result.data);
                    }
                }
            });
        }

        /**
         * 加载文章列表
         * @param size 每页条数
         * @param current 当前页数
         */
        function loadArticle(size=10, current=1) {
            $.ajax({
                url: contextPath + '/data/article/list',
                type: 'post',
                data: {
                    isDraft: 0,//查询非草稿文章
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
                                    loadArticle(obj.limit, obj.curr);
                                }
                            }
                        });
                        $('#articleItemList > .item').remove();
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

        function renderAnnouncement(data) {
            var view = $('#announcement').html();
            laytpl(view).render(data, function (html) {
                var $announcement = $('#articleItemList').children('.item-title.announcement');
                if ($announcement.length === 0) {
                    $('#articleItemList').prepend(html);
                } else {
                    $announcement.replaceWith(html);
                }
            });
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

        loadAnnouncement();
        loadArticle();
        blog.praise('', blog.praise.paramType.articleId);
    });
</script>
</body>
</html>