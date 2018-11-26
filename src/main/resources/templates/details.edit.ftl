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

        .error {
            line-height: 300px;
            font-size: 20px;
            text-align: center;
            display: block;
            color: #aaa;
        }
    </style>
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container">
        <#if (code == codeMap.CODE_ID_EMPTY )>
            <p class="error">文章id为空</p>
        <#elseif (code == codeMap.CODE_NO_PERMISSION )>
            <p class="error">无权编辑此文章</p>
        <#elseif (code == codeMap.CODE_NOT_EXIST )>
            <p class="error">文章不存在</p>
        <#else>
            <form id="articleForm" class="layui-form article-form" action="${contextPath}/data/article/${articleEdit.id???string("update","add")}" method="post">
                <div class="layui-form-item">
                    <input name="title" type="text" class="layui-input create-title-input" placeholder="请输入文章标题" value="${articleEdit.title!}">
                </div>
                <div class="layui-form-item">
                    <input type="hidden" id="id" name="id" value="${articleEdit.id!}">
                    <input type="hidden" id="fileIds" name="fileIds" value="${articleEdit.fileIds!}">
                    <textarea name="content" id="content" placeholder="文章内容" style="display: none;">${articleEdit.content!}</textarea>
                </div>
                <div class="layui-form-item create-tags-container">
                    <label class="layui-form-label">文章标签:</label>
                    <div class="layui-input-block">
                        <div class="inputTags">
                            <input type="hidden" name="tags" id="tags" value="${articleEdit.tags!}">
                            <input type="text" id="inputTags" class="inputTagsInput" placeholder="输入标签">
                        </div>
                    </div>
                </div>
            </form>
            <div class="create-button-group">
                <#if !articleEdit.id??>
                    <button id="publish" class="layui-btn layui-btn-normal">发布文章</button>
                    <button id="draft" class="layui-btn layui-btn-normal">保存草稿</button>
                    <button id="back" class="layui-btn create-button-back" onclick="window.history.go(-1)">返回</button>
                <#else>
                    <button id="publish" class="layui-btn layui-btn-normal">保存文章</button>
                    <button id="back" class="layui-btn create-button-back" onclick="window.history.go(-1)">返回</button>
                </#if>
            </div>
        </#if>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog',
        inputTags: '{/}${contextPath}/static/lib/layui-ext/input-tags/inputTags'
    }).use(['blog', 'layer', 'layedit', 'inputTags', 'jquery'], function () {
        var layedit = layui.layedit, layer = layui.layer;
        var inputTags = layui.inputTags;
        var $ = layui.jquery;

        $(document).on('click', '#publish', function (e) {
            e.preventDefault();
            publish();
        }).on('click', '#draft', function (e) {
            e.preventDefault();
            draft();
        });

        layedit.set({
            //暴露layupload参数设置接口 --详细查看layupload参数说明
            uploadImage: {
                url: contextPath + '/data/file/upload?layEditUpload=true',
                accept: 'image',
                acceptMime: 'image/*',
                exts: 'jpg|png|gif|bmp|jpeg',
                size: 0,
                done: function (res) {
                    //成功后的回调
                    var $fileIds = $('#fileIds');
                    var fileIds = $fileIds.val();
                    if (fileIds.trim() === '') {
                        fileIds = [];
                    } else {
                        fileIds = fileIds.split(',');
                    }
                    fileIds.push(res.data['fileId']);
                    $fileIds.val(fileIds.join(','));
                }
            }
            , uploadVideo: {
                url: contextPath + '/data/file/upload?layEditUpload=true',
                accept: 'video',
                acceptMime: 'video/*',
                exts: 'mp4|flv|avi|rm|rmvb',
                size: 0,
                done: function (res) {
                    //成功后的回调
                    var $fileIds = $('#fileIds');
                    var fileIds = $fileIds.val();
                    if (fileIds.trim() === '') {
                        fileIds = [];
                    } else {
                        fileIds = fileIds.split(',');
                    }
                    fileIds.push(res.data['fileId']);
                    $fileIds.val(fileIds.join(','));
                }
            }
            //右键删除图片/视频时的回调参数，post到后台删除服务器文件等操作，
            //传递参数：
            //图片： imgpath --图片路径
            //视频： filepath --视频路径 imgpath --封面路径
            , calldel: {
                url: contextPath + '/data/file/delete?layEditDelete=true<#if articleEdit.id??>&temporary=true</#if>',
                done: function (res) {
                    //成功后的回调
                    var $fileIds = $('#fileIds');
                    var fileIds = $fileIds.val();
                    if (fileIds.trim() === '') {
                        return;
                    } else {
                        fileIds = fileIds.split(',');
                    }
                    res.data.forEach(function (fileId) {
                        fileIds.splice(fileIds.indexOf(fileId), 1);
                    });
                    $fileIds.val(fileIds.join(','));
                }
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
                , '|', 'left', 'center', 'right', '|', 'link', 'unlink', 'image_alt', 'images', 'video', 'anchors'
                , '|', 'fullScreen'
            ]
        });
        var layeditIndex = layedit.build('content');

        var tags = $('#tags').val();
        inputTags.render({
            elem: '#inputTags',
            content: tags.trim().length > 0 ? tags.split(',') : [],
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
                        layer.alert('文章发布成功', function () {
                            window.location.href = contextPath + '/details/' + result.data;
                        });
                    } else {
                        layer.alert(result.message);
                    }
                }
            });
        }

        function draft() {
            layedit.sync(layeditIndex);
            var $articleForm = $('#articleForm');
            var formData = new FormData($articleForm.get(0));
            formData.append('isDraft', 1 + '');//设置为草稿状态
            $.ajax({
                url: $articleForm.attr('action'),
                type: $articleForm.attr('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.status === 'success') {
                        //todo 保存草稿后需要允许继续编辑
                        layer.alert('文章保存草稿成功', function () {
                            window.location.href = contextPath + '/details/' + result.data;
                        });
                    } else {
                        layer.alert('文章保存草稿失败，请稍后再试');
                    }
                }
            });
        }
    });
</script>
</body>
</html>