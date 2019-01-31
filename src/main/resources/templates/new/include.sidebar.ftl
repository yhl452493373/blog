<#include "include.common.ftl">
<#if announcement?has_content>
    <div class="whitebg notice">
        <h2 class="htitle">网站公告</h2>
        <div>${announcement.content}</div>
    </div>
</#if>
<#if hottest?has_content>
    <div class="whitebg paihang">
        <h2 class="htitle">点击排行</h2>
        <ul>
            <#list hottest as article>
                <li><i></i><a href="/">${article.title}</a></li>
            </#list>
        </ul>
    </div>
</#if>
<#if tags?has_content>
    <div class="whitebg cloud">
        <h2 class="htitle">标签云</h2>
        <ul>
            <#list tags as tag>
                <a href="" target="_blank">${tag.name}</a>
            </#list>
        </ul>
    </div>
</#if>
