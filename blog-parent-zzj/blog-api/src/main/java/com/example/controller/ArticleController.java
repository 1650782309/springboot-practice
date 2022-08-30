package com.example.controller;

import com.example.common.Cache.Cache;
import com.example.common.aop.LogAnnotation;
import com.example.service.ArticleService;
import com.example.vo.Result;
import com.example.vo.params.ArticleParam;
import com.example.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams
     */
    @PostMapping
    @LogAnnotation(module="文章",operation="获取文章列表")    //加上此注解 代表要队此接口记录日志
    //@RequestBody 接收数据打包成指定类
    @Cache(expire = 5 * 60 * 1000,name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页  最热文章
     * @return
     */
    @PostMapping("hot")
//    @LogAnnotation(module="文章",operation="获取最热文章")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页  最新文章
     * @return
     */
    @PostMapping("new")
//    @LogAnnotation(module="文章",operation="获取最新文章")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    /**
     * 文章归档
     * @return
     */
    @PostMapping("listArchives")
//    @LogAnnotation(module="文章",operation="获取文章归档")
    public Result listArticles(){
        return articleService.listArticles();
    }


    @PostMapping("view/{id}")
//    @LogAnnotation(module="文章",operation="查看文章")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
//    @LogAnnotation(module="文章",operation="发表文章")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }



}
