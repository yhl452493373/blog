<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>404 - 闲言轻博客</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <#include "include.resource.ftl">
</head>
<body class="lay-blog">
<#include "include.header.ftl">
<div class="container-wrap">
    <div class="container">
        <div class="contar-wrap">
            <div style="padding: 150px 0 50px; text-align: center; font-chunkSize: 30px; color: #ccc; font-weight: 300;">
                心姐抛出了个 404
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