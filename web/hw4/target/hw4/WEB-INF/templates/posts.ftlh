<#import "./common.ftlh" as c>

<@c.page>
    <div class="posts">
        <#if user_id??>
            <#list c.postsByUserId(user_id)?reverse as p>
                <@c.post p false false/>
            <#else>
                No such posts
            </#list>
        </#if>
    </div>
</@c.page>
