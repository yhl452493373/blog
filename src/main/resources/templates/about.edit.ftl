<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>关于-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="${contextPath}/static/lib/ace/ace.js"></script>
    <#include "include.resource.ftl">
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container container-message container-details container-about">
        <div class="item-box" style="max-height: none">
            <form id="aboutForm" class="layui-form about-form" action="${contextPath}/data/about/<#if aboutEdit.id??>update<#else>add</#if>" method="post">
                <div class="layui-form-item">
                    <input type="hidden" id="id" name="id" value="${aboutEdit.id!}">
                    <input type="hidden" id="fileIds" name="fileIds" value="${aboutEdit.fileIds!}">
                    <textarea name="content" id="content" placeholder="关于内容"
                              style="display: none;">${aboutEdit.content!}</textarea>
                </div>
            </form>
            <div class="save-button-group">
                <button id="save" class="layui-btn layui-btn-normal">保存</button>
                <button id="back" class="layui-btn create-button-back" onclick="window.history.go(-1)">返回</button>
            </div>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog'
    }).use(['blog', 'layer', 'layedit', 'jquery'], function () {
        var layedit = layui.layedit, layer = layui.layer;
        var $ = layui.jquery;

        $(document).on('click', '#save', function (e) {
            e.preventDefault();
            save();
        });

        layedit.set({
            //暴露layupload参数设置接口 --详细查看layupload参数说明
            uploadImage: {
                url: contextPath + '/data/file/upload',
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
                url: contextPath + '/data/file/upload',
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
                url: contextPath + '/data/file/delete?layEditDelete=true<#if aboutEdit.id??>&temporary=true</#if>',
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
            , height: 350
        });
        var layeditIndex = layedit.build('content');

        function save() {
            if (layedit.getContent(layeditIndex).length === 0 && layedit.getText(layeditIndex).length === 0) {
                layer.alert('请输入内容');
                return;
            }
            layedit.sync(layeditIndex);
            var $aboutForm = $('#aboutForm');
            var formData = new FormData($aboutForm.get(0));
            $.ajax({
                url: $aboutForm.attr('action'),
                type: $aboutForm.attr('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.status === 'success') {
                        layer.alert('保存成功', function () {
                            window.location.href = contextPath + '/about';
                        });
                    } else {
                        layer.alert('保存失败，请稍后再试');
                    }
                }
            });
        }
    });
</script>
</body>
</html>