<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>留言-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
    <#include "include.detail.ftl">
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
    <div class="info-item" data-belong-id="{{ d.id }}">
        <div class="info-text" style="padding-left: 0" data-id="{{ d.id }}">
            <p class="title count">
                <span class="name">
                    <@shiro.user>
                        <a class="layui-icon layui-icon-delete message-delete" href="${contextPath}/data/message/delete?id={{ d.id }}"></a>
                    </@shiro.user>
                    第{{ d.floor }}楼. {{= d.userName }} 于 {{ d.createdTime }} 留言:
                </span>
                <span class="info-img like" data-id="{{ d.id }}"><i class="layui-icon layui-icon-praise"></i><span class="count">{{ d.praiseCount }}</span></span>
            </p>
            <p class="info-intr">{{= d.content }}</p>
            <p style="text-align: right;line-height: 24px;padding-right: 10px;"><a href="#" class="message-reply">回复</a></p>
        </div>
        {{# layui.each(d.replyList, function(index, reply){ }}
        <div class="info-text info-reply" style="padding-left: 20px" data-id="{{ reply.id }}">
            <p class="title count" style="margin-top: 0">
                <span class="name">
                    <@shiro.user>
                        <a class="layui-icon layui-icon-delete message-delete" href="${contextPath}/data/message/delete?id={{ reply.id }}"></a>
                    </@shiro.user>
                    <span>第</span><span class="belong-floor">{{ reply.belongFloor }}</span><span>楼. </span>
                    <span class="username">{{= reply.userName }}</span><span> 于 </span>
                    <span class="created-time">{{ reply.createdTime }}</span>
                    <span> 回复:</span>
                </span>
                <span class="info-img like {{# if (reply.praised) { }}layblog-this{{# } }}" data-id="{{ reply.id }}" style="padding-right: 10px">
                    <i class="layui-icon layui-icon-praise"></i>
                    <span class="count">{{ reply.praiseCount }}</span>
                </span>
            </p>
            <p class="info-intr">{{= reply.content }}</p>
            <p style="text-align: right;line-height: 24px;"><a href="#" class="message-reply">回复</a></p>
        </div>
        {{# }); }}
    </div>
</script>
<script type="text/html" id="messageItemEmpty">
    <div class="info-item empty">
        {{= d.message }}
    </div>
</script>
<script type="text/html" id="replyBox">
    <form id="messageReplyForm" lay-filter="messageReplyForm" class="layui-form" style="display: none" action="${contextPath}/data/message/reply" method="post">
        <input type="hidden" name="belongId">
        <div class="layui-form-item layui-form-text">
            <textarea name="content" class="layui-textarea" style="resize: none" placeholder="说出您的心声"></textarea>
        </div>
        <div class="layui-form-item layui-text-right">
            <div class="layui-inline">
                <div class="layui-input-inline">
                    <#--noinspection FtlReferencesInspection-->
                    <input type="text" name="userName" value="<@shiro.principal property='username'/>"
                           placeholder="您的昵称" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <button class="layui-btn layui-btn-normal reply">回复</button>
                <button class="layui-btn layui-btn-primary cancel-reply">取消</button>
            </div>
        </div>
    </form>
</script>
<script>
    layui.use(['blog', 'jquery', 'laytpl', 'form', 'laypage'], function () {
        var $ = layui.jquery, laytpl = layui.laytpl, laypage = layui.laypage;
        var blog = layui.blog, form = layui.form;

        $(document).on('click', '#leaveMessage', function (e) {
            e.preventDefault();
            leaveMessage();
        }).on('click.message-delete', '.message-delete', function (e) {
            e.preventDefault();
            deleteMessage.call(this);
        }).on('click.message-reply', '.message-reply', function (e) {
            e.preventDefault();
            replyMessageBox.call(this);
        });

        function replyMessageBox() {
            var $belongMessage = $(this).closest('.info-text');
            var hideReplyForm = function () {
                $('#messageReplyForm').slideUp(function () {
                    $(this).remove();
                });
            };
            if ($belongMessage.find('#messageReplyForm').length !== 0) {
                hideReplyForm();
                return;
            }
            hideReplyForm();
            var defaultText = function () {
                var texts = [];
                if ($belongMessage.find('.belong-floor').length !== 0) {
                    texts.push('回复 ');
                    texts.push($belongMessage.find('.belong-floor').text().trim() + '# ');
                    texts.push($belongMessage.find('.username').text().trim());
                    texts.push(': ');
                }
                return texts.join('');
            }();
            var view = $('#replyBox').html();
            //模板渲染
            laytpl(view).render({}, function (html) {
                var $replyForm = $(html);
                $belongMessage.append($replyForm);
                $replyForm.slideDown();
                form.val('messageReplyForm', {
                    belongId: $belongMessage.parent().data('belongId'),
                    content: defaultText
                });
                $belongMessage.on('click.reply', '.reply', function (e) {
                    e.preventDefault();
                    replyMessage.call(document.getElementById('messageReplyForm'), defaultText);
                }).on('click.cancel-reply', '.cancel-reply', function (e) {
                    e.preventDefault();
                    hideReplyForm();
                });
            });
        }

        function replyMessage(defaultText) {
            var formData = new FormData(this);
            if (formData.get("userName").trim().length === 0) {
                layer.msg('昵称不能为空');
                return;
            } else if (formData.get("content").trim().length === 0) {
                layer.msg('回复内容不能为空');
                return;
            }
            if (formData.get("content").trim().indexOf(defaultText.trim()) === -1) {
                formData.set("content", defaultText + formData.get("content"));
            }
            $.ajax({
                url: this.getAttribute('action'),
                type: this.getAttribute('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.status === 'success') {
                        layer.alert('回复成功!', function (index) {
                            layer.close(index);
                            loadMessage();
                        });
                    } else {
                        layer.alert(result.message);
                    }
                }
            });
        }

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
                            initPraised();
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
            if (formData.get("userName").trim().length === 0) {
                layer.msg('昵称不能为空');
                return;
            } else if (formData.get("content").trim().length === 0) {
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

        function initPraised() {
            var $praiseCounts = $('span.count');
            $praiseCounts.each(function () {
                var $praiseCount = $(this).closest('.like');
                var itemId = $praiseCount.data('id');
                if (localStorage.getItem(itemId + '_praised') === 'yes') {
                    $praiseCount.addClass('layblog-this');
                }
            });
        }

        loadMessage();

        blog.praise('.info-img', blog.praise.paramType.message);
    });
</script>
</body>
