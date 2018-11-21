<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>撰写-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
    <style>
        .create-tags-container {
            background-color: #fff;
            border: 1px solid #e6e6e6;
        }
    </style>
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <form id="articleForm" class="layui-form article-form" action="${contextPath}/data/article/add" method="post">
        <div class="layui-form-item">
            <input name="title" type="text" class="layui-input create-title-input" placeholder="请输入文章标题">
        </div>
        <div class="layui-form-item">
            <textarea name="content" id="content" placeholder="文章内容" style="display: none;"></textarea>
        </div>
        <div class="layui-form-item create-tags-container">
            <label class="layui-form-label">文章标签:</label>
            <div class="layui-input-block">
                <div class="inputTags">
                    <input type="hidden" name="tags" id="tags">
                    <input type="text" id="inputTags" class="inputTagsInput" placeholder="输入标签">
                </div>
            </div>
        </div>
    </form>
    <div class="create-button-group">
        <button id="publish" class="layui-btn layui-btn-normal">发布文章</button>
        <button id="draft" class="layui-btn layui-btn-normal">保存草稿</button>
        <button id="back" class="layui-btn create-button-back" onclick="window.history.go(-1)">返回</button>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog',
        inputTags: '{/}${contextPath}/static/lib/layui-ext/input-tags/inputTags'
    }).use(['blog', 'layedit', 'inputTags', 'jquery'], function () {
        var layedit = layui.layedit;
        var inputTags = layui.inputTags;
        var $ = layui.jquery;

        $(document).on('click', '#publish', function (e) {
            e.preventDefault();
            publish();
        });

        layedit.set({
            uploadImage: {
                url: contextPath + '/data/file/upload?layEditUpload=true' //图片上传接口url,此接口需要返回
                /**
                 * {
                 *    "code": 0 //0表示成功，其它失败
                 *    ,"msg": "" //提示信息 //一般上传失败后返回
                 *    ,"data": {
                 *      "src": "图片路径"
                 *      ,"title": "图片名称" //可选
                 *    }
                 *  }
                 */
                , type: 'post' //get or post
                , down: function (data) {
                    //data -- 接口返回数据中的data
                }
            }
        });

        var layeditIndex = layedit.build('content', {
            height: 400
        }); //建立编辑器

        inputTags.render({
            elem: '#inputTags',
            content: [],
            aldaBtn: false,
            focus: false,
            done: function () {
                $('#tags').val(this.get().join(','));
            },
            del: function () {
                $('#tags').val(this.get().join(','));
            }
        });

        function publish() {
            layedit.sync(layeditIndex);
            var $articleForm = $('#articleForm');
            var formData = new FormData($articleForm.get(0));
            $.ajax({
                url: $articleForm.attr('action'),
                type: $articleForm.attr('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    console.log(result);
                }
            })
        }
    });
</script>
</body>
</html>