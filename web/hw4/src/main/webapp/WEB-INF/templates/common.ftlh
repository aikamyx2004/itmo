<#-- @ftlvariable name="current_uri" type="java.lang.String" -->
<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if logged_user_id??>
                <#assign u = findBy(users, "id", logged_user_id)/>
                <@userlinkNameOnly u/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <@navigation/>
    </header>
</#macro>

<#macro navigation>
    <nav>
        <#assign items=[
        {"href": "/index", "name": "Home"},
        {"href": "/users", "name": "Users"},
        {"href": "/misc/help", "name": "Help"},
        {"href": "/contests", "name": "Contests"}
        ]/>
        <ul>
            <#list items as item>
                <li>
                    <#if current_uri == item.href>
                        <a class="underline" href="${item.href}">${item.name}</a>
                    <#else>
                        <a href="${item.href}">${item.name}</a>
                    </#if>
                </li>
            </#list>
        </ul>
    </nav>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as p>
            <section>
                <@post p true true/>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>


<#macro userlinkText user text>
    <a class="user-link" href="/user?handle=${user.handle}">${text}</a>
</#macro>

<#macro userlinkNameOnly user>
    <a class="user-link" href="/user?handle=${user.handle}">${user.handle}</a>
</#macro>

<#macro userlink user>
    <a style="color:${user.color}" class="user-link" href="/user?handle=${user.handle}">${user.handle}</a>
</#macro>

<#macro postLink p text>
    <a href="/post?post_id=${p.id}">${text}</a>
</#macro>

<#function postsByUserId user_id>
    <#return posts?filter(p -> p.user_id == user_id)/>
</#function>

<#macro post p needShrink isAside>
    <#assign u = findBy(users, "id", p.user_id)/>
    <article>
        <#if isAside>
            <div class="title">
                <@postLink p "Post #${p.id}"/>
            </div>
        <#else>
            <div class="title">
                <@postLink p p.title/>
            </div>
            <div class="information">By <@userlink u/>, translation</div>
        </#if>
        <div class="body">
            <#if needShrink && p.text?length gt 350>
                <#assign lines = (p.text?substring(0, 350) + "...")?split("\n")>
            <#else>
                <#assign lines = p.text?split("\n")>
            </#if>

            <#list lines as line>
                <p>${line}</p>
            </#list>
            <#if isAside>
            <div class="footer">
                <a href="/post?post_id=${p.id}"> View all</a>
            </div>
            <#else>
        </div>
        <div class="footer">
            <div class="left">
                <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+173</span>
                <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
        </#if>
    </article>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function findNext items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return items[item_index + 1]/>
        </#if>
    </#list>
</#function>

<#function findPrevious items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return items[item_index - 1]/>
        </#if>
    </#list>
</#function>

