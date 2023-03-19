<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if user_id??>
        <#assign u = c.findBy(users, "id", user_id)!/>
    <#elseif handle??>
        <#assign u = c.findBy(users, "handle", handle)!/>
    </#if>

    <#if u?has_content>
        <div class="user">
            <@c.userlink u/>
            <ul>
                <li>id: ${u.id}</li>
                <li>handle: ${u.handle}</li>
                <li>name: ${u.name}</li>
                <li>
                    <#assign userPosts = c.postsByUserId(u.id)?size>
                    <a style="text-decoration: none" href="/posts?user_id=#{u.id}">${userPosts} posts</a>
                </li>
            </ul>
            <img class="icon" src="/img/user-icon.jpg" alt="${u.handle} icon">
        </div>

        <div class="arrows">
            <#assign prevU = c.findPrevious(users, "handle", u.handle)!/>
            <@user_arrow prevU "⟵"></@user_arrow>

            <#assign nextU = c.findNext(users, "handle", u.handle)!/>
            <@user_arrow nextU "⟶"></@user_arrow>
        </div>
    <#else>
        No such user
    </#if>
</@c.page>


<#macro user_arrow user arrow>
    <#if user?has_content>
        <@c.userlinkText user arrow/>
    <#else>
        <span>${arrow}</span>
    </#if>
</#macro>