<#include "include.common.ftl">
<#--富文本编辑器需要的内容-->
<link rel="stylesheet" href="${contextPath}/static/lib/codemirror/css/codemirror.min.css">
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/froala_editor.css">
<#--由于resource中引用了,这个不需要再引用-->
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
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/help.css">
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/third_party/spell_checker.css">
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/special_characters.css">
<#--富文本编辑器需要的内容-->
<script type="text/javascript" src="${contextPath}/static/lib/jquery/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="${contextPath}/static/lib/html2pdf/html2pdf.bundle.js"></script>
<script type="text/javascript" src="${contextPath}/static/lib/codemirror/js/codemirror.min.js"></script>
<script type="text/javascript" src="${contextPath}/static/lib/codemirror/js/xml.min.js"></script>
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

