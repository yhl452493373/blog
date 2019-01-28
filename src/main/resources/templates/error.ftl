<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${code!"500"} - 闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container">
        <div class="contar-wrap">
            <h3>${message}</h3>
            <div style="padding: 15px 0 50px; text-align: left; color: #666; ">
                ${detail!"异常页面"}
            </div>
        </div>
    </div>
</div>
<#include "include.footer.ftl">
</body>
<script>
    layui.use(['blog'], function () {
        var blog = layui.blog;
    });
</script>
</html>