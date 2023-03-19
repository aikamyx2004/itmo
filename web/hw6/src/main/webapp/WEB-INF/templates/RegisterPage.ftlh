<#-- @ftlvariable name="email" type="java.lang.String" -->
<#-- @ftlvariable name="passwordConfirmation" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->
<#-- @ftlvariable name="password" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Register</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="register"/>
                <@field "Login" "login" "login" login!/>
                <@field "Email" "email" "email" email!/>
                <@field "Password" "password" "password" password!/>
                <@field "Confirm the password" "passwordConfirmation" "password" passwordConfirmation!/>
<#--                <div class="field">-->
<#--                    <div class="name">-->
<#--                        <label for="login">Login</label>-->
<#--                    </div>-->
<#--                    <div class="value">-->
<#--                        <input id="login" name="login" value="${login!}"/>-->
<#--                    </div>-->
<#--                </div>-->
<#--                <div class="field">-->
<#--                    <div class="name">-->
<#--                        <label for="password">Password</label>-->
<#--                    </div>-->
<#--                    <div class="value">-->
<#--                        <input id="password" name="password" type="password" value="${password!}"/>-->
<#--                    </div>-->
<#--                </div>-->
<#--                <div class="field">-->
<#--                    <div class="name">-->
<#--                        <label for="email">Email</label>-->
<#--                    </div>-->
<#--                    <div class="value">-->
<#--                        <input id="email" name="email" type="email" value="${email!}"/>-->
<#--                    </div>-->
<#--                </div>-->
<#--                <div class="field">-->
<#--                    <div class="name">-->
<#--                        <label for="passwordConfirmation">Confirm the password</label>-->
<#--                    </div>-->
<#--                    <div class="value">-->
<#--                        <input id="passwordConfirmation" name="passwordConfirmation" type="password" value="${passwordConfirmation!}"/>-->
<#--                    </div>-->
<#--                </div>-->
                <#if error??>
                    <div class="error">
                        ${error!}
                    </div>
                </#if>
                <div class="button-field">
                    <input type="submit" value="Login">
                </div>
            </form>
        </div>
    </div>
    <#macro field text name type value>
        <div class="field">
            <div class="name">
                <label for="${name}">${text}</label>
            </div>
            <div class="value">
                <input id="${name}" name="${name}" type="${type}" value="${value}"/>
            </div>
        </div>
    </#macro>
</@c.page>
