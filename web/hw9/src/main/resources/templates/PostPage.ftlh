<#-- @ftlvariable name="comment" type="ru.itmo.wp.domain.Comment" -->
<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="post">
        <#if post??>
            <@c.post post/>
            <#if user??>
                <div class="comment form">
                    <div class="header">Comments</div>
                    <div class="body">
                        <form method="post" action="">
                            <input type="hidden" name="action" value=""/>
                            <div class="field">
                                <div class="name">
                                    <label for="text"></label>
                                </div>
                                <div class="value">
                                    <textarea id="text" name="text">${comment.text!}</textarea>
                                </div>
                                <@c.error "comment.text"/>
                            </div>
                            <div class="button-field">
                                <input type="submit" value="Send">
                            </div>
                        </form>
                    </div>
                </div>
            </#if>
            <div class="comments">
                <#list post.comments as comment>
                    <@createComment comment/>
                </#list>
            </div>
        <#else>
            No such post
        </#if>
    </div>
    <#macro createComment comment>
        <div class="comment">
            <div class="caption">By ${comment.user.login}</div>
            <div class="body">${comment.text}</div>
        </div>
    </#macro>
</@c.page>
