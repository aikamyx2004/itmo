<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <div class="noData">
            No data
        </div>
        <template>
            <article>
                <div class="title"></div>
                <div class="information"></div>
                <div class="body"></div>
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
        </template>
    </div>
    <script>
        $(function () {
            const $noData = $(".articles .noData");
            $noData.text("Loading...");
            ajax({action: "getArticles"},
                function (response) {
                    response["data"].forEach(function (data) {
                        const article = data["article"];
                        const user = data["user"];
                        if (!article["hidden"]) {
                            $noData.hide();
                            const $article = $($(".articles template").prop("content")).clone();
                            $article.find(".title").text(article["title"]);
                            $article.find(".information").text("By " + user + ", translation");
                            $article.find(".body").text(article["text"]);
                            $(".articles").append($article);
                        }
                    })
                    return false;
                })
        })
    </script>
</@c.page>
