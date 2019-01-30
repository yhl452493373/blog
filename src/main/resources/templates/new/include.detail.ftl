<#include "include.common.ftl">
<#--富文本编辑器需要用到的样式-->
<link rel="stylesheet" href="${contextPath}/static/css/font-awesome.min.css">
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/froala_style.css">

<#--ace代码编辑器需要的内容,展示代码样式也需要用到-->
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/plugins/code.css">
<script src="${contextPath}/static/lib/ace/ace.js"></script>
<script src="${contextPath}/static/lib/ace/ext-language_tools.js"></script>
<script src="${contextPath}/static/lib/ace/ext-static_highlight.js"></script>

<script>
    layui.use(['blog'], function () {
        layui.blog.renderCode();
    });
</script>