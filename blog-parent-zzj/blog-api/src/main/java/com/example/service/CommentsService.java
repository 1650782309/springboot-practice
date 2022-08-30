package com.example.service;

import com.example.vo.Result;
import com.example.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 根据文章id查询所有评论列表
     * @param id
     * @return
     */
    Result commentsByArticledId(Long id);

    Result comment(CommentParam commentParam);
}
