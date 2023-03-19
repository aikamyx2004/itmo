<#import "commons.ftlh" as c>

<@c.page>
    <#macro createArticle data>
        <#assign article = data.article/>
        <#assign userLogin = data.user/>
        <#if article.hidden>
            <article>
                <div class="title">${article.title}</div>
                <div class="information">By ${userLogin} translation</div>
                <div class="body">
                    <#list article.text?split("\n") as line>
                        <p>${line}</p>
                    </#list>
                </div>
                <div class="footer">
                    <div class="left">
                        <img src="/img/voteup.png" title="Vote Up" alt="Vote Up"/>
                        <span class="positive-score">+173</span>
                        <img src="/img/votedown.png" title="Vote Down" alt="Vote Down"/>
                    </div>
                    <div class="right">
                        <img src="/img/comments_16x16.png" title="Comments" alt="Comments"/>
                        <a href="#">68</a>
                    </div>
                </div>
            </article>

        </#if>

    </#macro>
    <table>

    </table>
</@c.page>