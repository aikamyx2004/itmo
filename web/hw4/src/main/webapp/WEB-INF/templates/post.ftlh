<#import "./common.ftlh" as c>

<@c.page>
    <div class="post">
        <#if post_id??>
            <#assign p = c.findBy(posts, "id", post_id)!/>
            <#if p?has_content>
                <@c.post p false false/>
            <#else>
                No such post
            </#if>
        <#else>
            No such post
        </#if>
    </div>
</@c.page>