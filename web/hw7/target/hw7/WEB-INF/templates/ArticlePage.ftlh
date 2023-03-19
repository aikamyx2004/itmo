<#import "commons.ftlh" as c>

<@c.page>
    <div class="article form-box">
        <div class="header">Article</div>
        <div class="body">
            <form method="post" action="">
                <input type="hidden" name="action" value="article"/>
                <div class="field">
                    <div class="name">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input id="title" name="title" value="${title!}"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="text">text</label>
                    </div>
                    <div class="value">
                        <textarea name="text" id="text" cols="30" rows="10">${text!}</textarea>
                    </div>
                </div>
                <div class="error"></div>
                <div class="button-field">
                    <input type="submit" value="Create Article">
                </div>
            </form>
        </div>
    </div>
    <script>
        $(function () {
            const user = $(this).find(".enter-or-register-box .userLogin").val();
            if (user === undefined) {
                const $body = $(this).find("main");
                $body.text("You have to enter before creating articles")
                return;
            }

            $(".article form").submit(function () {
                const title = $(this).find("input[name='title']").val();
                const text = $(this).find("textarea[name='text']").val();
                const $error = $(this).find(".error");
                ajax({action: "createArticle", title, text},
                    function (response, $errorJQ = $error) {
                        if (response.hasOwnProperty("error")) {
                            $errorJQ.text(response["error"]);
                            return false;
                        }
                        return true;
                    });
                return false;
                // $.ajax({
                //     type: "POST",
                //     url: "",
                //     dataType: "json",
                //     data: {action: "createArticle", title, text},
                //     success: function (response) {
                //         if (response.hasOwnProperty("error")) {
                //             $error.text(response["error"]);
                //         } else {
                //             if (response.hasOwnProperty("redirect")) {
                //                 location.href = response["redirect"];
                //             }
                //         }
                //         return false;
                //     }
                // });
            });
        })
    </script>
</@c.page>