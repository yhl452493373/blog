<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文章-闲言轻博客</title>
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
                        ${article.title?html}
                    </h3>
                    <h5>发布于：<span>${article.publishTime?string('yyyy-MM-dd HH:mm:ss')}</span></h5>
                    <h6 style="font-size: 12px">
                        <#list tagList as tag>
                            <a href="#" title="${tag.name?html}相关文章" style="color: #999">${tag.name?html}</a>
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
    <div class="info-item" data-belong-id="{{ d.id }}">
        <#--头像-->
        <#--<img class="info-img" src="../res/static/images/info-img.png" alt="">-->
        <div class="info-text" style="padding-left: 0" data-id="{{ d.id }}">
            <p class="title count" style="margin-top: 0">
                <span class="name">
                    <@shiro.user>
                        <a class="layui-icon layui-icon-delete comment-delete" href="${contextPath}/data/comment/delete?id={{ d.id }}"></a>
                    </@shiro.user>
                    <span>第</span><span class="floor">{{ d.floor }}</span><span>楼. </span>
                    <span class="username">{{= d.userName }}</span><span> 于 </span>
                    <span class="created-time">{{ d.createdTime }}</span>
                    <span> 评论:</span>
                </span>
                <span class="info-img like {{# if (d.praised) { }}layblog-this{{# } }}" data-id="{{ d.id }}">
                    <i class="layui-icon layui-icon-praise"></i>
                    <span class="count">{{ d.praiseCount }}</span>
                </span>
            </p>
            <p class="info-intr">{{= d.content }}</p>
            <p style="text-align: right;line-height: 24px;"><a href="#" class="comment-reply">回复</a></p>
        </div>
        {{# layui.each(d.replyList, function(index, reply){ }}
        <div class="info-text info-reply" style="padding-left: 20px" data-id="{{ reply.id }}">
            <p class="title count" style="margin-top: 0">
                <span class="name">
                    <@shiro.user>
                        <a class="layui-icon layui-icon-delete comment-delete" href="${contextPath}/data/comment/delete?id={{ reply.id }}"></a>
                    </@shiro.user>
                    <span>第</span><span class="belong-floor">{{ reply.belongFloor }}</span><span>楼. </span>
                    <span class="username">{{= reply.userName }}</span><span> 于 </span>
                    <span class="created-time">{{ reply.createdTime }}</span>
                    <span> 评论:</span>
                </span>
                <span class="info-img like {{# if (reply.praised) { }}layblog-this{{# } }}" data-id="{{ reply.id }}">
                    <i class="layui-icon layui-icon-praise"></i>
                    <span class="count">{{ reply.praiseCount }}</span>
                </span>
            </p>
            <p class="info-intr">{{= reply.content }}</p>
            <p style="text-align: right;line-height: 24px;"><a href="#" class="comment-reply">回复</a></p>
        </div>
        {{# }); }}
    </div>
</script>
<script type="text/html" id="commentItemEmpty">
    <div class="info-item empty">
        {{ d.message }}
    </div>
</script>
<script type="text/html" id="replyBox">
    <form id="commentReplyForm" lay-filter="commentReplyForm" class="layui-form" style="display: none" action="${contextPath}/data/comment/reply" method="post">
        <input type="hidden" name="articleId">
        <input type="hidden" name="replyId">
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
                <button class="layui-btn layui-btn-normal reply">发表</button>
                <button class="layui-btn layui-btn-primary cancel-reply">取消</button>
            </div>
        </div>
    </form>
</script>
<script>
    var articleId = '${article.id}';

    layui.use(['blog', 'jquery', 'laytpl', 'laypage', 'form'], function () {
        var $ = layui.jquery, laytpl = layui.laytpl, laypage = layui.laypage, form = layui.form;
        var blog = layui.blog;

        $(document).on('click.article-delete', '.article-delete', function (e) {
            e.preventDefault();
            deleteArticle.call(this);
        }).on('click.comment-delete', '.comment-delete', function (e) {
            e.preventDefault();
            deleteComment.call(this);
        }).on('click.comment-reply', '.comment-reply', function (e) {
            e.preventDefault();
            replyCommentBox.call(this);
        });

        function replyCommentBox() {
            var $belongComment = $(this).closest('.info-text');
            var hideReplyForm = function () {
                $('#commentReplyForm').slideUp(function () {
                    $(this).remove();
                });
            };
            if ($belongComment.find('#commentReplyForm').length !== 0) {
                hideReplyForm();
                return;
            }
            hideReplyForm();
            var defaultText = function () {
                var texts = [];
                if ($belongComment.find('.belong-floor').length !== 0) {
                    texts.push('回复 ');
                    texts.push($belongComment.find('.belong-floor').text().trim() + '# ');
                    texts.push($belongComment.find('.username').text().trim());
                    texts.push(': ');
                }
                return texts.join('');
            }();
            var view = $('#replyBox').html();
            //模板渲染
            laytpl(view).render({}, function (html) {
                var $replyForm = $(html);
                $belongComment.append($replyForm);
                $replyForm.slideDown();
                form.val('commentReplyForm', {
                    belongId: $belongComment.parent().data('belongId'),
                    content: defaultText,
                    articleId: articleId
                });
                $belongComment.on('click.reply', '.reply', function (e) {
                    e.preventDefault();
                    replyComment.call(document.getElementById('commentReplyForm'), defaultText);
                }).on('click.cancel-reply', '.cancel-reply', function (e) {
                    e.preventDefault();
                    hideReplyForm();
                });
            });
        }

        function replyComment(defaultText) {
            var formData = new FormData(this);
            if (formData.get("userName").trim().length === 0) {
                layer.msg('昵称不能为空');
                return;
            } else if (formData.get("content").trim().length === 0) {
                layer.msg('评论内容不能为空');
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
                        });
                    } else {
                        layer.alert(result.message);
                    }
                }
            });
        }

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