package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dao.dos.Archives;
import com.example.dao.mapper.ArticleBodyMapper;
import com.example.dao.mapper.ArticleMapper;
import com.example.dao.mapper.ArticleTagMapper;
import com.example.dao.mapper.CategoryMapper;
import com.example.dao.pojo.Article;
import com.example.dao.pojo.ArticleBody;
import com.example.dao.pojo.ArticleTag;
import com.example.dao.pojo.SysUser;
import com.example.service.*;
import com.example.utils.UserThreadLocal;
import com.example.vo.*;
import com.example.vo.params.ArticleParam;
import com.example.vo.params.PageParams;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImep implements ArticleService {

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }
//        @Override
//    public Result listArticle(PageParams pageParams){
//        /**
//         * 1、分页查询article表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize()); //传入页面和大小
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();//new一个设置查询页面规则的类
//        if (pageParams.getCategoryId()!=null){
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId()!=null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags){
//                articleIdList.add(articleTag.getArticleId());
//            }
//            queryWrapper.in(Article::getId,articleIdList);
//
//        }
//        //设定规则，倒序排列 ：：lambda表达式 引用相应静态方法
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);//置顶排序
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);//查找
//        List<Article> records = articlePage.getRecords();
//
//        //封装成vo对象返回
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//
//        return Result.success(articleVoList);
//
//    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor){
        ArrayList<ArticleVo> articleVoArrayList  = new ArrayList<>();
        for (Article recode : records){
            articleVoArrayList.add(copy(recode,isTag,isAuthor,false,false));
        }
        return articleVoArrayList;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArrayList<ArticleVo> articleVoArrayList  = new ArrayList<>();
        for (Article recode : records){
            articleVoArrayList.add(copy(recode,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoArrayList;
    }

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有接口都需要标签，作者信息
        if (isTag){
            Long id = article.getId();
            articleVo.setTags(tagService.fingTagsByArtticleId(id));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));

        }

        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory( categoryService.findCategoryById(categoryId));
        }


        return articleVo;

    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();//规则设置
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);//注意拼接加空格
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);//传入规则设置，查询到数据封装成list

        return Result.success(copyList(articles,false,false,false,false));
    }


    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();//规则设置
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);//注意拼接加空格
        //select id,title from article order by creat_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);//传入规则设置，查询到数据封装成list

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArticles() {
       List<Archives> articleList = articleMapper.listArchives();
        return Result.success(articleList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1、根据id查询 文章信息
         * 2、根据bodyId和categoryid去做关联查询
         */

        Article article = this.articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true,true,true);
        //查看完文章，新增阅读数   更新时加写锁 阻塞其他读的操作 性能低
        //更新    增加了此次接口的耗时  如果一旦更新出问题   不能影响查看文章的操作
        //线程池解决 可以把更新操作扔到线程池中执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }


    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();

        boolean isEdit = false;
        if (articleParam.getId()!=null){
            article = new Article();
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
            isEdit = true;
        }else {
            article  = new Article();
            article.setAuthorId(sysUser.getId());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            this.articleMapper.insert(article);
        }
//
//
//
//        article.setAuthorId(sysUser.getId());
//        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
//        article.setCreateDate(System.currentTimeMillis());
//        article.setCommentCounts(0);
//        article.setSummary(articleParam.getSummary());
//        article.setTitle(articleParam.getTitle());
//        article.setViewCounts(0);
//        article.setWeight(Article.Article_Common);
//        article.setBodyId(-1L);
//        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                if (isEdit){
                    LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(ArticleTag::getArticleId,articleId);
                    articleTagMapper.delete(queryWrapper);
                }
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(Long.parseLong(tag.getId()));
                this.articleTagMapper.insert(articleTag);
            }
        }

//body
        if (isEdit){
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            LambdaQueryWrapper<ArticleBody> updateWapper = new LambdaQueryWrapper<>();
            updateWapper.eq(ArticleBody::getArticleId,article.getId());
            articleBodyMapper.update(articleBody,updateWapper);
        }else {
            ArticleBody articleBody = new ArticleBody();
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            articleBody.setArticleId(article.getId());
            articleBodyMapper.insert(articleBody);
            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);
        }
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
//        ArticleVo articleVo = new ArticleVo();
//        articleVo.setId(String.valueOf(article.getId()));
        if (isEdit){
            //发送一条消息给rocketmq，文章更新了,更新缓存
            ArticleMessage articleMessage = new ArticleMessage();
            articleMessage.setArticleId(article.getId());
            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);

            
        }
        return Result.success(map);
    }
}
