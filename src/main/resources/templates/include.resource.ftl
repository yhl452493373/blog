<#include "include.common.ftl">
<#--css-->
<link rel="stylesheet" href="${contextPath}/static/lib/layui/css/layui.css">
<link rel="stylesheet" href="${contextPath}/static/css/main.css">
<#--富文本编辑器需要用到的样式-->
<link rel="stylesheet" href="${contextPath}/static/css/font-awesome.min.css">
<link rel="stylesheet" href="${contextPath}/static/lib/froala-editor/css/froala_style.css">

<#--ace代码编辑器需要的内容-->
<script src="${contextPath}/static/lib/ace/ace.js"></script>
<script src="${contextPath}/static/lib/ace/ext-language_tools.js"></script>
<script src="${contextPath}/static/lib/ace/ext-static_highlight.js"></script>

<#--script-->
<script src="${contextPath}/static/lib/layui/layui.js"></script>

<style>
    .search-popup .layui-layer-content {
        padding: 10px 0;
        background: #fff;
    }

    .search-popup .layui-layer-content .search-item-box {
        background: #fff;
        padding: 10px;
        box-sizing: border-box;
        height: 100%;
        overflow: auto;
    }

    .search-popup .layui-layer-content .search-item {
        border-bottom: 1px solid #f2f2f2;
        background-color: #fff;
        height: 135px;
        overflow: hidden;
        margin: 10px;
        position: relative;
    }

    .search-popup .layui-layer-content .search-item * {
        word-break: break-all;
    }

    .search-popup .layui-layer-content .search-item-info {
        position: absolute;
        bottom: 0;
        width: 100%;
        height: 30px;
        line-height: 30px;
        background: #fff;
        font-size: 14px;
    }

    .search-popup .layui-layer-content .search-item-content > h3:first-child {
        overflow: hidden;
        text-overflow: ellipsis;
        word-break: break-all;
        white-space: nowrap;
    }

    .search-popup .layui-layer-content .search-item-content .preview-content,
    .search-popup .layui-layer-content .search-item-content .preview-content *{
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        max-height: 65px;
    }

    .search-popup .layui-layer-content .search-item-info span:first-child {
        float: left;
        color: #666;
    }

    .search-popup .layui-layer-content .search-item-info span:last-child {
        float: right;
        color: #666;
    }
</style>

<script>
    var contextPath = '${contextPath}';
</script>
