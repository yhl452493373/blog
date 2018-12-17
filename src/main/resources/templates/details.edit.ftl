<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>撰写-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.3.0/codemirror.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/froala_editor.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/froala_style.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/code_view.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/draggable.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/colors.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/emoticons.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/image_manager.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/image.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/line_breaker.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/table.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/char_counter.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/video.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/fullscreen.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/file.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/quick_insert.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/help.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/third_party/spell_checker.css">
    <link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/special_characters.css">

    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.3.0/codemirror.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.3.0/mode/xml/xml.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/jquery/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/froala_editor.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/align.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/char_counter.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/code_beautifier.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/code_view.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/colors.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/draggable.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/emoticons.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/entities.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/file.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/font_size.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/font_family.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/fullscreen.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/image.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/image_manager.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/line_breaker.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/inline_style.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/link.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/lists.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/paragraph_format.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/paragraph_style.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/quick_insert.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/quote.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/table.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/save.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/url.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/video.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/help.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/print.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/special_characters.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/plugins/word_paste.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/lib/froala-editor/js/languages/zh_cn.js"></script>

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
    }).use(['blog', 'layer', 'inputTags', 'jquery'], function () {
        var layer = layui.layer, inputTags = layui.inputTags;
        var $ = layui.jquery;

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
            toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', '|', 'fontFamily', 'fontSize', 'color', 'inlineClass', 'inlineStyle', 'paragraphStyle', 'lineHeight', '|', 'paragraphFormat', 'align', 'formatOL', 'formatUL', 'outdent', 'indent', 'quote', '-', 'insertLink', 'insertImage', 'insertVideo', 'embedly', 'insertFile', 'insertTable', '|', 'emoticons', 'fontAwesome', 'specialCharacters', 'insertHR', 'selectAll', 'clearFormatting', '|', 'print', 'help', 'html', '|', 'undo', 'redo'],
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
            videoAllowedTypes: ['webm', 'jpg', 'ogg']
        }).on('froalaEditor.file.beforeUpload', function (e, editor, files) {
            // Return false if you want to stop the file upload.
        }).on('froalaEditor.file.uploaded', function (e, editor, response) {
            // File was uploaded to the server.
        }).on('froalaEditor.file.inserted', function (e, editor, $file, response) {
            // File was inserted in the editor.
        }).on('froalaEditor.file.error', function (e, editor, error, response) {
            // Bad link.
            if (error.code === 1) {
            }

            // No link in upload response.
            else if (error.code === 2) {
            }

            // Error during file upload.
            else if (error.code === 3) {
            }

            // Parsing response failed.
            else if (error.code === 4) {
            }

            // File too text-large.
            else if (error.code === 5) {
            }

            // Invalid file type.
            else if (error.code === 6) {
            }

            // File can be uploaded only to same domain in IE 8 and IE 9.
            else if (error.code === 7) {
            }

            // Response contains the original server response to the request if available.
        }).on('froalaEditor.image.beforeUpload', function (e, editor, images) {
            // Return false if you want to stop the image upload.
        }).on('froalaEditor.image.uploaded', function (e, editor, response) {
            // Image was uploaded to the server.
        }).on('froalaEditor.image.inserted', function (e, editor, $img, response) {
            // Image was inserted in the editor.
        }).on('froalaEditor.image.replaced', function (e, editor, $img, response) {
            // Image was replaced in the editor.
        }).on('froalaEditor.image.error', function (e, editor, error, response) {
            // Bad link.
            if (error.code === 1) {
            }

            // No link in upload response.
            else if (error.code === 2) {
            }

            // Error during image upload.
            else if (error.code === 3) {
            }

            // Parsing response failed.
            else if (error.code === 4) {
            }

            // Image too text-large.
            else if (error.code === 5) {
            }

            // Invalid image type.
            else if (error.code === 6) {
            }

            // Image can be uploaded only to same domain in IE 8 and IE 9.
            else if (error.code === 7) {
            }

            // Response contains the original server response to the request if available.
        }).on('froalaEditor.video.beforeUpload', function (e, editor, videos) {
            // Return false if you want to stop the video upload.
        }).on('froalaEditor.video.uploaded', function (e, editor, response) {
            // Video was uploaded to the server.
        }).on('froalaEditor.video.inserted', function (e, editor, $img, response) {
            // Video was inserted in the editor.
        }).on('froalaEditor.video.replaced', function (e, editor, $img, response) {
            // Video was replaced in the editor.
        }).on('froalaEditor.video.error', function (e, editor, error, response) {
            // Bad link.
            if (error.code === 1) {
            }

            // No link in upload response.
            else if (error.code === 2) {
            }

            // Error during video upload.
            else if (error.code === 3) {
            }

            // Parsing response failed.
            else if (error.code === 4) {
            }

            // Video too text-large.
            else if (error.code === 5) {
            }

            // Invalid video type.
            else if (error.code === 6) {
            }

            // Video can be uploaded only to same domain in IE 8 and IE 9.
            else if (error.code === 7) {
            }

            // Response contains the original server response to the request if available.
        });

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