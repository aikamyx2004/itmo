<#import "commons.ftlh" as c>

<@c.page>
    <div class="notice form-box">
        <div class="header">Notice</div>
        <div class="body">
            <form method="post" action="">
                <div class="field">
                    <div class="name">
                        <label for="content">content</label>
                    </div>
                    <div class="value">
                        <textarea name="content" id="content" cols="30" rows="10"></textarea>
                    </div>
                    <@c.error "noticeForm.content"/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Add notice">
                </div>
            </form>
        </div>
    </div>
</@c.page>