<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>撰写-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
    <script src="${contextPath}/static/lib/ace/ace.js"></script>
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
    <div class="container">
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
            //暴露layupload参数设置接口 --详细查看layupload参数说明
            uploadImage: {
                url: '${contextPath}/data/file/upload?layEditUpload=true',
                accept: 'image',
                acceptMime: 'image/*',
                exts: 'jpg|png|gif|bmp|jpeg',
                size: 1024 * 1024 * 2 + ''
            }
            , uploadVideo: {
                url: '${contextPath}/data/file/upload?layEditUpload=true',
                accept: 'video',
                acceptMime: 'video/*',
                exts: 'mp4|flv|avi|rm|rmvb',
                size: 1024 * 1024 * 10 + ''
            }
            //右键删除图片/视频时的回调参数，post到后台删除服务器文件等操作，
            //传递参数：
            //图片： imgpath --图片路径
            //视频： filepath --视频路径 imgpath --封面路径
            , calldel: {
                url: '${contextPath}/data/file/delete?layEditDelete=true'
            }
            //开发者模式 --默认为false
            , devmode: false
            //插入代码设置
            , codeConfig: {
                hide: false,  //是否显示编码语言选择框
                default: 'javascript' //hide为true时的默认语言格式
            }
            , tool: [
                'html', 'code', 'strong', 'italic', 'underline', 'del', 'addhr', '|', 'fontFomatt', 'colorpicker', 'face'
                , '|', 'left', 'center', 'right', '|', 'link', 'unlink', 'image_alt', 'video', 'anchors'
                , '|', 'fullScreen'
            ]
        });
        var layeditIndex = layedit.build('content');

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
                    if (result.status === 'success') {

                    }
                }
            })
        }
    });
</script>
</body>
</html>