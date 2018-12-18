<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>关于-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
    <#include "include.froalaeditor.ftl">
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
    }).use(['blog', 'layer', 'layedit'], function () {
        var  layer = layui.layer;

        //富文本编辑器
        $('#content').froalaEditor({
            language: 'zh_cn',
            pasteDeniedAttrs: true,
            pasteDeniedTags: true,
            spellcheck: false,
            toolbarSticky: false,
            htmlExecuteScripts: false,
            height: 300,
            fontFamily: {
                'Andale Mono': 'Andale Mono',
                'Arial,Helvetica,sans-serif': 'Arial',
                'Georgia,serif': 'Georgia',
                'Impact,Charcoal,sans-serif': 'Impact',
                'Sans-Serif': 'Sans-Serif',
                'Tahoma,Geneva,sans-serif': 'Tahoma',
                'Times New Roman,Times,serif': 'Times New Roman',
                'Verdana,Geneva,sans-serif': 'Verdana',
                '宋体,SimSun': '宋体',
                '楷体,楷体_GB2312,SimKai': '楷体',
                '黑体,SimHei': '黑体',
                '隶书, SimLi': '隶书',
                '微软雅黑,Microsoft YaHei': '微软雅黑'
            },
            paragraphFormat: {
                H1: 'Heading 1',
                H2: 'Heading 2',
                H3: 'Heading 3',
                H4: 'Heading 4',
                N: 'Normal',
                PRE: 'Code'
            },
            paragraphStyles: {
                'fr-text-indent': 'Text Indent',
                'fr-text-gray': 'Gray',
                'fr-text-bordered': 'Bordered',
                'fr-text-spaced': 'Spaced',
                'fr-text-uppercase': 'Uppercase'
            },
            toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', '|', 'fontFamily', 'fontSize', 'color', 'inlineClass', 'inlineStyle', 'paragraphStyle', 'lineHeight', '|', 'paragraphFormat', 'align', 'formatOL', 'formatUL', 'quote', '-', 'outdent', 'indent', 'insertLink', 'insertImage', 'insertVideo', 'embedly', 'insertFile', 'insertTable', '|', 'emoticons', 'fontAwesome', 'specialCharacters', 'insertHR', 'selectAll', 'clearFormatting', '|', 'print', 'getPDF', 'html', '|', 'undo', 'redo'],
            fileUploadParam: 'file',
            fileUploadURL: contextPath + '/data/file/upload',
            fileUploadMethod: 'POST',
            fileMaxSize: 5 * 1024 * 1024,//最大单个文件5MB
            imageUploadParam: 'file',
            imageUploadURL: contextPath + '/data/file/upload',
            imageUploadMethod: 'POST',
            imageMaxSize: 5 * 1024 * 1024,//最大单个图片5MB
            imageAllowedTypes: ['jpeg', 'jpg', 'png'],
            videoUploadParam: 'file',
            videoUploadURL: contextPath + '/data/file/upload',
            videoUploadMethod: 'POST',
            videoMaxSize: 20 * 1024 * 1024,//最大单个视频5MB
            videoAllowedTypes: ['webm', 'mp4', 'ogg']
        }).on('froalaEditor.file.uploaded', function (e, editor, response) {
            uploadedCallback(response);
        }).on('froalaEditor.file.unlink', function (e, editor, link) {
            deleteCallback($(link), 'file');
        }).on('froalaEditor.file.error', function (e, editor, error, response) {
            errorCallback(error, response);
        }).on('froalaEditor.image.uploaded', function (e, editor, response) {
            uploadedCallback(response);
        }).on('froalaEditor.image.removed', function (e, editor, $img) {
            deleteCallback($img, 'image');
        }).on('froalaEditor.image.error', function (e, editor, error, response) {
            errorCallback(error, response);
        }).on('froalaEditor.video.uploaded', function (e, editor, response) {
            uploadedCallback(response);
        }).on('froalaEditor.video.removed', function (e, editor, $video) {
            deleteCallback($video, 'video');
        }).on('froalaEditor.video.error', function (e, editor, error, response) {
            errorCallback(error, response);
        });

        function uploadedCallback(response) {
            var $fileIds = $('#fileIds');
            var fileIds = $fileIds.val();
            if (fileIds.trim() === '') {
                fileIds = [];
            } else {
                fileIds = fileIds.split(',');
            }
            response = JSON.parse(response);
            fileIds.push(response.data['fileId']);
            $fileIds.val(fileIds.join(','));
        }

        function deleteCallback($object, objectType) {
            var objMap = {
                video: 'src',
                image: 'src',
                file: 'href'
            };
            var url = $object.attr(objMap[objectType]);
            if (url == null)
                url = $object.find(objectType).attr(objMap[objectType]);
            if (url != null) {
                $.ajax({
                    method: "POST",
                    url: contextPath + '/data/file/delete',
                    data: {
                        sourceUrls: url,
                        temporary: true
                    }
                }).done(function (res) {
                    console.log(objectType + ' was deleted');
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
                }).fail(function () {
                    console.log(objectType + ' delete failed');
                });
            }
        }

        function errorCallback(error, response) {
            if (error.code === 1) {
                layer.alert('Bad Link');
            } else if (error.code === 2) {
                layer.alert('No link in upload response');
            } else if (error.code === 3) {
                layer.alert('Error during upload');
            } else if (error.code === 4) {
                layer.alert('Parsing response failed');
            } else if (error.code === 5) {
                layer.alert('File too text-large');
            } else if (error.code === 6) {
                layer.alert('Invalid file type')
            } else if (error.code === 7) {
                layer.alert('File can be uploaded only to same domain in IE 8 and IE 9');
            }
            console.log(response);
        }

        $(document).on('click', '#save', function (e) {
            e.preventDefault();
            save();
        });

        function save() {
            if ($('#content').val().length === 0) {
                layer.alert('请输入内容');
                return;
            }
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