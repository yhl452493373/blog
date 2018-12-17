<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>撰写-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
    <#include "include.froalaeditor.ftl">
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
        <input name="title" type="text" class="layui-input create-title-input" placeholder="请输入文章标题" value="<#if isDraft!>[草稿] </#if>${articleEdit.title!}">
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
            <#if !articleEdit.id?? || isDraft!>
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
    }).use(['blog', 'layer', 'inputTags'], function () {
        var layer = layui.layer, inputTags = layui.inputTags;

        $(document).on('click', '#publish', function (e) {
            e.preventDefault();
            publish();
        }).on('click', '#draft', function (e) {
            e.preventDefault();
            draft();
        });

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
            var $articleForm = $('#articleForm');
            var formData = new FormData($articleForm.get(0));
            formData.append('isDraft', 0);//设置为非草稿状态
            formData.append("planTextContent", $('#content').val());
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
            var $articleForm = $('#articleForm');
            var formData = new FormData($articleForm.get(0));
            formData.append('isDraft', 1);//设置为草稿状态
            formData.append("planTextContent", $('#content').val());
            $.ajax({
                url: $articleForm.attr('action'),
                type: $articleForm.attr('method'),
                data: formData,
                processData: false,
                contentType: false,
                success: function (result) {
                    if (result.status === 'success') {
                        //todo 保存草稿后需要允许继续编辑
                        $('#id').val(result.data);
                        $('#articleForm').attr('action', contextPath + '/data/article/update');
                        layer.alert('文章保存草稿成功');
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