<#--这里是常规上传示例,适合小文件-->
<#--<#include "include.common.ftl">-->
<#--<!DOCTYPE html>-->
<#--<html lang="en">-->
<#--<head>-->
<#--<meta charset="UTF-8">-->
<#--<title>文件上传测试</title>-->
<#--</head>-->
<#--<body>-->
<#--<form action="${contextPath}/data/file/upload" method="post" enctype="multipart/form-data">-->
<#--<input type="file" name="file">-->
<#--<button type="submit">上传</button>-->
<#--</form>-->
<#--</body>-->
<#--</html>-->

这里是大文件分片上传示例
<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文件上传测试</title>
<#include "include.resource.ftl">
</head>
<body>
<form action="${contextPath}/data/file/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" id="file">
</form>
<button id="submit">上传</button>
<script>
    layui.use(['jquery'], function () {
        var $ = layui.jquery;

        var upload = function (file, taskId, chunk=0) {
            var formData = new FormData();//初始化一个FormData对象
            var chunkSize = 1024 * 1024 * 2;//每块的大小(字节)
            var chunkTotal = Math.ceil(file.size / chunkSize);//总分片数
            var nextSize = Math.min((chunk + 1) * chunkSize, file.size);//读取到结束位置
            var fileData = file.slice(chunk * chunkSize, nextSize);//截取 部分文件 块
            if (taskId)
                formData.append("taskId", taskId);//将 部分文件 塞入FormData
            formData.append("file", fileData);//将 部分文件 塞入FormData
            formData.append("fileName", file.name);//将 部分文件 塞入FormData
            formData.append("fileType", file.type);//将 部分文件 塞入FormData
            formData.append("fileSize", file.size);//将 部分文件 塞入FormData
            formData.append("chunk", chunk + '');//当前为第几分片
            formData.append("chunkSize", chunkSize + '');//每个分片大小
            formData.append("chunkTotal", chunkTotal + '');//总分片数
            $.ajax({
                url: "${contextPath}/data/file/upload",
                type: "post",
                data: formData,
                processData: false,  // 告诉jQuery不要去处理发送的数据
                contentType: false,   // 告诉jQuery不要去设置Content-Type请求头
                success: function (result) {
                    if (file.size <= nextSize) {//如果上传完成，则跳出继续上传
                        alert("上传完成");
                        return;
                    }
                    if (result.status === 'success') {
                        upload(file, result.data['taskId'], ++chunk);//递归调用
                    } else {
                        alert(result.message)
                    }
                }
            });
        };

        $('#submit').click(function () {
            var file = $("#file")[0].files[0];
            upload(file);
        });
    });
</script>
</body>
</html>