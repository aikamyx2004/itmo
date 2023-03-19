package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;

import java.util.List;
import java.util.Map;

public interface ArticleRepository {
    void save(Article article);
    List<Map<String, Object>> findAll();
}
