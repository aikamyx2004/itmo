<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Send message</div>
        <form method="post" action="">
            <input type="hidden" name="action" value="send"/>
            <div class="field">
                <div class="name">
                    <label for="targetUser">Target user</label>
                </div>
                <div class="value">
                    <input id="targetUser" name="targetUser"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">text</label>
                </div>
                <div class="value">
                    <input id="text" name="text" type="text"/>
                </div>
            </div>
            <#if error??>
                <div class="error">
                    ${error!}
                </div>
            </#if>
            <div class="button-field">
                <input type="submit" value="Send">
            </div>
        </form>
    </div>

    <div class="messages">
        <div class="datatable">
            <div class="caption">Talks</div>
            <table>
                <thead>
                <tr>
                    <th>Source user</th>
                    <th>Target user</th>
                    <th>Text</th>
                    <th>Time</th>
                </tr>
                </thead>
                <tbody>
                <#list data as talk>
                    <tr>
                        <td>${talk.sourceUser}</td>
                        <td>${talk.targetUser}</td>
                        <td>${talk.text}</td>
                        <td>${talk.creationTime}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

</@c.page>