<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
<title>关于</title>
  <#include "include.resource.ftl">
</head>
<body>
<!--top begin-->
<#include "include.header.ftl">
<!--top end-->
<article>
  <div class="whitebg about">
    <h2 class="gd_title">个人简介</h2>
    <div class="ab_box">
      ${aboutRead.content}
    </div>
    <sub style="float: right">更新时间:&nbsp;${aboutRead.createdTime?datetime}</sub>
  </div>
</article>
<#include "include.footer.ftl">
</body>
</html>
