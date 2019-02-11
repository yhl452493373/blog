<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>留言</title>
    <link rel="stylesheet" href="${contextPath}/static/lib/layui/css/layui.css">
    <script src="${contextPath}/static/lib/layui/layui.js"></script>
    <#include "include.resource.ftl">
    <style>
        .message{
            padding-left: 35px!important;
        }

        .message-reply{
            border-bottom-style: dashed!important;
        }

        .fbinfo{
            word-break: break-all;
        }

        .layui-form-label{
            width: 50px;
        }

        .layui-input-block{
            margin-left: 80px;
        }

        @media screen and (max-width: 450px){
            .layui-form-item .layui-input-inline{
                margin-left: 82px!important;
            }
        }
    </style>
</head>
<body>
<!--top begin-->
<#include "include.header.ftl">
<!--top end-->
<article>
    <!--lbox begin-->
    <div class="lbox">
        <div style="background-color: #fff">
            <div class="gbook" id="messageList"></div>
            <div class="list-page" id="page"></div>
            <div class="gbox">
                <form id="messageForm" class="layui-form" action="/data/message/add" method="post">
                    <div class="layui-form-item">
                        <label class="layui-form-label">昵称:</label>
                        <div class="layui-input-inline">
                            <input type="text" name="userName" value="" placeholder="您的昵称" class="layui-input">
                        </div>
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <label class="layui-form-label">内容:</label>
                        <div class="layui-input-block">
                            <textarea class="layui-textarea" name="content" placeholder="留下你想说的话吧"></textarea>
                        </div>
                    </div>
                    <input type="submit" id="leaveMessage" value="提交">
                </form>
            </div>
        </div>
    </div>
    <div class="rbox">
        <#include "include.sidebar.ftl">
    </div>
</article>
<#include "include.footer.ftl">
<script type="text/html" id="itemListTemplate">
    {{# layui.each(d.itemList, function(index, message){ }}
        <div class="fb">
            <ul class="message">
                <li>
                    <p class="fbtime">
                        <span>第&nbsp;{{ message.floor }}&nbsp;楼</span><span>&nbsp;</span>
                        <span>{{ message.createdTime }}</span><span>&nbsp;</span>
                        <span>{{ message.userName }}:</span>
                    </p>
                    <p class="fbinfo">{{= message.content }}</p>
                </li>
            </ul>
            {{# layui.each(message.replyList, function(index, reply){ }}
                {{# if(reply.userId!=null){ }}
                    <ul class="message-reply">
                        <li>
                            <p class="zzhf">
                                <span>第&nbsp;{{ reply.belongFloor }}&nbsp;楼</span><span>&nbsp;</span>
                                <span>{{ message.createdTime }}</span><span>&nbsp;</span>
                                <span style="color: #FF0000">博主回复:</span>
                                <br>
                            </p>
                            <p class="fbinfo">{{= reply.content }}</p>
                        </li>
                    </ul>
                {{# } else { }}
                    <ul class="message-reply">
                        <li>
                            <p class="fbtime">
                                <span>第&nbsp;{{ reply.belongFloor }}&nbsp;楼</span><span>&nbsp;</span>
                                <span>{{ reply.createdTime }}</span><span>&nbsp;</span>
                                <span>{{ reply.userName }}</span>
                            </p>
                            <p class="fbinfo">{{= reply.content }}</p>
                        </li>
                    </ul>
                {{# } }}
            {{# }); }}
        </div>
    {{# }); }}
</script>
<script>
    layui.use(['jquery', 'laytpl', 'laypage', 'layer'], function () {
        var $ = layui.jquery, laytpl = layui.laytpl, laypage = layui.laypage, layer = layui.layer;

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
        function loadMessage(size=5, current=1) {
            $.ajax({
                url: 'data/message/list',
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
                            , layout: ['count', 'prev', 'page', 'next']
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
                        renderData({itemList: result.data});
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

        function renderData(itemList) {
            var view = $('#itemListTemplate').html();
            //模板渲染
            laytpl(view).render(itemList, function (html) {
                $('#messageList').html(html);
            });
        }

        loadMessage();
    });
</script>
</body>
</html>
