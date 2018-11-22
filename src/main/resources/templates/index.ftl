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
        <#--<div class="item">-->
        <#--<div class="item-box  layer-photos-demo2 layer-photos-demo">-->
        <#--<h3><a href="${contextPath}/details">拥有诗意的心态,才能拥有诗意的生活</a></h3>-->
        <#--<h5>发布于：<span>刚刚</span></h5>-->
        <#--<p>-->
        <#--父爱如山，不善表达。回想十多年前，总记得父亲有个宽厚的肩膀，小小的自己跨坐在上面，越过人山人海去看更广阔的天空，那个时候期望自己有一双翅膀，能够像鸟儿一样飞得高，看得远。虽然父亲有时会和自己开玩笑，但在做错事的时候会受到严厉的训斥。父亲有双粗糙的大手掌，手把手教我走路、骑车，却会在该放手的时刻果断地放开让自己去大胆尝试，那个时候期望快快长大，能够做自己想做的事，不用受父亲的“控制”。父亲是智慧树，他无所不知、无所不晓，虽然你有十万个为什么，但是也难不倒他。</p>-->
        <#--<img src="${contextPath}/static/images/item.png" alt="">-->
        <#--</div>-->
        <#--<div class="comment count">-->
        <#--<a href="${contextPath}/details#comment">评论</a>-->
        <#--<a href="javascript:;" class="like">点赞</a>-->
        <#--</div>-->
        <#--</div>-->
        <#--<div class="item">-->
        <#--<div class="item-box  layer-photos-demo3 layer-photos-demo">-->
        <#--<h3><a href="${contextPath}/details">拥有诗意的心态,才能拥有诗意的生活</a></h3>-->
        <#--<h5>发布于：<span>刚刚</span></h5>-->
        <#--<p>-->
        <#--父爱如山，不善表达。回想十多年前，总记得父亲有个宽厚的肩膀，小小的自己跨坐在上面，越过人山人海去看更广阔的天空，那个时候期望自己有一双翅膀，能够像鸟儿一样飞得高，看得远。虽然父亲有时会和自己开玩笑，但在做错事的时候会受到严厉的训斥。父亲有双粗糙的大手掌，手把手教我走路、骑车，却会在该放手的时刻果断地放开让自己去大胆尝试，那个时候期望快快长大，能够做自己想做的事，不用受父亲的“控制”。父亲是智慧树，他无所不知、无所不晓，虽然你有十万个为什么，但是也难不倒他。</p>-->
        <#--<img src="${contextPath}/static/images/item.png" alt="">-->
        <#--</div>-->
        <#--<div class="comment count">-->
        <#--<a href="${contextPath}/details#comment">评论</a>-->
        <#--<a href="javascript:;" class="like">点赞</a>-->
        <#--</div>-->
        <#--</div>-->
        </div>
        <div class="item-btn">
            <button class="layui-btn layui-btn-normal">下一页</button>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script type="text/html" id="articleItem">
    <div class="item">
        <div class="item-box">
            <h3><a href="${contextPath}/details/{{ d.id }}">{{ d.title }}</a></h3>
            <h5>发布于：<span>{{ d.publishTime }}</span></h5>
            <div class="preview-content">{{ d.content }}</div>
        </div>
        <div class="comment count">
            <a href="${contextPath}/comment/{{ d.id }}">评论</a>
            <a href="javascript:;" class="like">点赞</a>
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

        function loadData() {
            $.ajax({
                url: contextPath + '/data/article/list',
                type: 'post',
                data: {
                    isDraft: 0//查询非草稿文章
                },
                success: function (result) {
                    if (result.status === 'success') {
                        if (result.data.length > 0) {
                            result.data.forEach(function (item) {
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

        loadData();
    });
</script>
</body>
</html>