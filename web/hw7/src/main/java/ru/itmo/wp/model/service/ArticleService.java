package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;

import java.util.List;
import java.util.Map;

public class ArticleService {
    ArticleRepository articleRepository = new ArticleRepositoryImpl();

    public void save(Article article) {
        articleRepository.save(article);
    }
    public List<Map<String, Object>> findAll(){
        return articleRepository.findAll();
    }

    public void validate(Article article) throws ValidationException {
        if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().isBlank()) {
            throw new ValidationException("Title can not be empty");
        }
        if (article.getTitle().length() > 255) {
            throw new ValidationException("Title can not be longer than 255 symbols");
        }
        if (Strings.isNullOrEmpty(article.getText()) || article.getText().isBlank()) {
            throw new ValidationException("Text can not be empty");
        }
        if (article.getText().length() > 2048) {
            throw new ValidationException("Text can not be longer than 2048 symbols");
        }
    }
}
