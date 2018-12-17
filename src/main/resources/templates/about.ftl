<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>关于-闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container container-message container-details container-about">
        <div class="contar-wrap">
            <div class="item" style="max-height: none">
                <div class="item-box" style="max-height: none">
                    <h3>
                        关于
                        <@shiro.user>
                            <a class="layui-icon layui-icon-edit" href="${contextPath}/about/edit"></a>
                        </@shiro.user>
                    </h3>
                    <#if aboutRead??>
                        <div class="item-conent fr-view">
                            ${aboutRead.content}
                        </div>
                    <#else>
                        <p style="text-align: center">此人很懒,什么都没写.</p>
                    </#if>
                    <#--<div class="count layui-clear">-->
                    <#--<span class="pull-left">阅读 <em>100000+</em></span>-->
                    <#--<span class="pull-right like"><i class="layui-icon layui-icon-praise"></i><em>999</em></span>-->
                    <#--</div>-->
                </div>
            </div>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
<script>
    layui.extend({
        blog: '{/}${contextPath}/static/lib/layui-ext/blog/blog'
    }).use('blog');
</script>
</body>
</html>