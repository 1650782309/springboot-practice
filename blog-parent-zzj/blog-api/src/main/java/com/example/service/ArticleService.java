package com.example.service;

import com.example.vo.Result;
import com.example.vo.params.ArticleParam;
import com.example.vo.params.PageParams;

public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 首页 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 首页 最新文章
     * @param limit
     * @return
     */

    Result newArticles(int limit);

    /**
     * 首页 文章归档
     * @return
     */
    Result listArticles();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}


