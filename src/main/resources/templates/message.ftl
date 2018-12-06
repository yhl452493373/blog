<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>留言-闲言轻博客</title>
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

        #messageItemList {
            background-color: #fff;
            padding: 10px;
            margin-top: 30px !important;
        }
    </style>
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container container-message">
        <div class="contar-wrap" id="contar-wrap">
            <form id="messageForm" class="layui-form" action="${contextPath}/data/message/add" method="post">
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <div class="layui-input-inline">
                            <input type="text" name="userName" value="<@shiro.principal property='username'/>"
                                   placeholder="您的昵称" class="layui-input">
                        </div>
                    </div>
                </div>
                <div class="layui-form-item layui-form-text">
                    <textarea class="layui-textarea" name="content" placeholder="留下你想说的话吧"></textarea>
                </div>
            </form>
            <div class="item-btn">
                <button class="layui-btn layui-btn-normal" id="leaveMessage">写好了</button>
            </div>

            <div id="LAY-msg-box">
                <div class="info-box" id="messageItemList">

                </div>
            </div>

            <div id="page"></div>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script type="text/html" id="messageItem">
    <div class="info-item">
        <#--头像,未处理-->
        <#--<img class="info-img" src="${contextPath}/static/images/info-img.png" alt="">-->
        <div class="info-text" style="padding-left: 0">
            <p class="title count">
                <span class="name">
                    <@shiro.user>
                        <a class="layui-icon layui-icon-delete message-delete" href="${contextPath}/data/message/delete?id={{ d.id }}"></a>
                    </@shiro.user>
                    第{{ d.floor }}楼. {{ d.userName }} 于 {{ d.createdTime }} 留言:
                </span>
                <span class="info-img like" data-id="{{ d.id }}"><i class="layui-icon layui-icon-praise"></i><span class="count">{{ d.praiseCount }}</span></span>
            </p>
            <p class="info-intr">{{ d.content }}</p>
        </div>
    </div>
</script>
<script type="text/html" id="messageItemEmpty">
    <div class="info-item empty">
        {{ d.message }}
    </div>
</script>
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog'
    }).use(['blog', 'jquery', 'laytpl', 'laypage'], function () {
        var $ = layui.jquery, laytpl = layui.laytpl, laypage = layui.laypage;
        var blog = layui.blog;

        $(document).on('click', '#leaveMessage', function (e) {
            e.preventDefault();
            leaveMessage();
        }).on('click.message-delete', '.message-delete', function (e) {
            e.preventDefault();
            deleteMessage.call(this);
        });

        function deleteMessage() {
            var href = this.getAttribute('href');
            layer.confirm('确定删除这条留言?', function (index) {
                $.ajax({
                    url: href,
                    success: function (result) {
                        if (result.status === 'success') {
                            layer.alert('删除成功', function (index) {
                                loadMessage();
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

        /**
         * 加载留言列表
         * @param size 每页条数
         * @param current 当前页数
         */
        function loadMessage(size=10, current=1) {
            $.ajax({
                url: contextPath + '/data/message/list',
                type: 'post',
                data: {
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
                                    loadMessage(obj.limit, obj.curr);
                                }
                            }
                        });
                        $('#messageItemList > .info-item').remove();
                        if (result.data.length > 0) {
                            result.data.forEach(function (item) {
                                renderData(item);
                            });
                        } else {
                            renderEmpty({
                                message: '还没有人留言,快来抢沙发!'
                            });
                        }
                    }
                }
            })
        }

        function leaveMessage() {
            var $messageForm = $('#messageForm');
            var formData = new FormData($messageForm.get(0));
            if(formData.get("userName").trim().length===0){
                layer.msg('昵称不能为空');
                return;
            }else if(formData.get("content").trim().length===0){
                layer.msg('留言内容不能为空');
                return;
            }
            $.ajax({
                url: $messageForm.attr('action'),
                type: $messageForm.attr('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.status === 'success') {
                        layer.alert('留言成功!', function (index) {
                            layer.close(index);
                            $messageForm.get(0).reset();
                            loadMessage();
                        });
                    } else {
                        layer.alert(result.message);
                    }
                }
            });
        }

        function renderData(item) {
            var view = $('#messageItem').html();
            //模板渲染
            laytpl(view).render(item, function (html) {
                $('#messageItemList').children('.item-info.empty').remove().end().append(html);
            });
        }

        function renderEmpty(item) {
            var $items = $('#messageItemList').children('.item-info');
            if ($items.length === 0) {
                var view = $('#messageItemEmpty').html();
                //模板渲染
                laytpl(view).render(item, function (html) {
                    $('#messageItemList').append(html);
                });
            }
        }

        loadMessage();

        blog.praise('.info-img', blog.praise.paramType.message);
    });
</script>
</body>
