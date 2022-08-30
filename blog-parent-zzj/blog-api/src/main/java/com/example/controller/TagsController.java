package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.TagService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    @GetMapping("hot")
//    @LogAnnotation(module="标签",operation="获取最热标签")
    public Result hot(){
        int limit = 6;
       return tagService.hots(limit);
    }

    @GetMapping
//    @LogAnnotation(module="标签",operation="查询所有标签")
    public Result findAll(){
        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }
}
