<#import "./common.ftlh" as c>

<@c.page>
    <div class="users">
        <div class="caption">Users</div>
        <table>
            <thead>
            <tr>
                <th>id</th>
                <th>handle</th>
                <th>name</th>
            </tr>
            </thead>
            <tbody>
            <#list users as u>
                <tr>
                    <td>${u.id}</td>
                    <td><@c.userlink u/></td>
                    <td>${u.name}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
