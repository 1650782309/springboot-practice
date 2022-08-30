package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.CommentsService;
import com.example.vo.Result;
import com.example.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class commentController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
//    @LogAnnotation(module="评论",operation="获取评论")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticledId(id);
    }

    @PostMapping("create/change")
//    @LogAnnotation(module="评论",operation="发表评论")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }


}


