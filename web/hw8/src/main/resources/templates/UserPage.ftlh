<#import "commons.ftlh" as c>

<@c.page>
    <#if showUser??>
        <div class="user datatable">
            <div class="caption">User</div>
            <table>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Login</th>
                    <th>Created</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="id">${showUser.id}</td>
                    <td class="login">${showUser.login}</td>
                    <td class="creationTime">${showUser.creationTime}</td>
                </tr>
                </tbody>
            </table>
        </div>
    <#else>
        No such user
    </#if>
</@c.page>
