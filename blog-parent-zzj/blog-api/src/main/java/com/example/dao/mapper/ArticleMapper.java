package com.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dao.dos.Archives;
import com.example.dao.pojo.Article;
import com.example.vo.params.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    IPage<Article> listArticle(@Param("page") Page<Article> page,
                              @Param("categoryId") Long categoryId,
                              @Param("tagId") Long tagId,
                              @Param("year") String year,
                              @Param("month") String month);
}
