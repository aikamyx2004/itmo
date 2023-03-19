package ru.itmo.wp.web.page;

import org.checkerframework.checker.units.qual.A;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ArticlePage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = getUser(request);
        if (user == null) {
            throw new ValidationException("You have to enter before creating articles");
        }
    }

    private void createArticle(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        Article article = new Article();
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("text"));
        article.setHidden(false);
        User user = getUser(request);
        if (user == null) {
            throw new ValidationException("You have to enter before creating articles");
        }
        article.setUserId(user.getId());
        articleService.validate(article);
        articleService.save(article);

        request.getSession().setAttribute("message", "You successfully created article!");
        throw new RedirectException("/article");
    }
    private User getUser(HttpServletRequest request){
        return (User) request.getSession().getAttribute("user");
    }
}
