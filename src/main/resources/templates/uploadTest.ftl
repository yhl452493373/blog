<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文件上传测试</title>
</head>
<body>
<form action="${contextPath}/data/file/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <button type="submit">上传</button>
</form>
</body>
</html>