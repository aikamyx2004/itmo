<#-- @ftlvariable name="posts" type="ru.itmo.web.hw4.model.Post[]" -->
<#import "./common.ftlh" as c>

<@c.page>
    <div class="posts">
        <ul>
            <#list posts?reverse as p>
                <@c.post p true false/>
            </#list>
        </ul>
    </div>
</@c.page>
