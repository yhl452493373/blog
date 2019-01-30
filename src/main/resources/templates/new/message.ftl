<#include "include.common.ftl">
<!DOCTYPE html>
<html lang="zh">
<head>
    <title>留言</title>
    <#include "include.resource.ftl">
</head>
<body>
<!--top begin-->
<#include "include.header.ftl">
<!--top end-->
<article>
    <!--lbox begin-->
    <div class="lbox">
        <div class="gbook">
            <div class="fb">
                <ul>
                    <p class="fbtime"><span> 2018-07-21 </span> 夜月归途</p>
                    <p class="fbinfo">从青姐朋友圈分享的文章《我为什么要做个人网站》过来的，自习看了下你的网站非常不错，看的出来你一直在坚持!</p>
                </ul>
            </div>
            <div class="hf">
                <ul>
                    <p class="zzhf"><font color="#FF0000">站长回复:</font>感谢捧场啊！看了你的网站，有两个月没更新了哦~</p>
                </ul>
            </div>
            <div class="fb">
                <ul>
                    <p class="fbtime"><span> 2018-07-10 </span> 周宏</p>
                    <p class="fbinfo">读《从今日起，我永久卸载今日头条》有感。正如作者所说，这个APP抓住了很多人性的特点，在简单、重复、爽这三点上做到了极致。但是我认为永久卸载这个想法比较荒诞，任何东西你只要有自控力，就能将它为我所用。曾经一度我也是刷头条根本停不下来</p>
                </ul>
            </div>
            <div class="hf">
                <ul>
                    <p class="zzhf"><font color="#FF0000">站长回复:</font>嗯，我也是自制力有限，删除头条就是矫枉过正而已，这个因人而异，不强求他人，也不想标题党。</p>
                </ul>
            </div>
            <div class="fb">
                <ul>
                    <p class="fbtime"><span> 2018-07-09 </span> 文颖</p>
                    <p class="fbinfo">加油吖</p>
                </ul>
            </div>
            <div class="hf">
                <ul>
                    <p class="zzhf"><font color="#FF0000">站长回复:</font>感谢第一条留言的支持！：）</p>
                </ul>
            </div>
            <div class="gbox">
                <form action="../../enews/index.php" method="post" name="form1" id="form1">
                    <p> <strong>来说点儿什么吧...</strong></p>
                    <p><span> 您的姓名:</span>
                        <input name="name" type="text" id="name">
                        *</p>
                    <p><span>联系邮箱:</span>
                        <input name="email" type="text" id="email">
                        *</p>
                    <p><span class="tnr">留言内容:</span>
                        <textarea name="lytext" cols="60" rows="12" id="lytext"></textarea>
                    </p>
                    <p>
                        <input type="submit" name="Submit3" value="提交">
                        <input name="enews" type="hidden" id="enews" value="AddGbook">
                    </p>
                </form>
            </div>
        </div>
    </div>
    <div class="rbox">
        <#include "include.sidebar.ftl">
    </div>
</article>
<#include "include.footer.ftl">
</body>
</html>
