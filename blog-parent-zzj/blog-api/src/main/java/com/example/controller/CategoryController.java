package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.CategoryService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
//    @LogAnnotation(module="类别",operation="获取类别")
    public Result categories() {
       return categoryService.findAll();

    }

    @GetMapping("detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDeatailById(@PathVariable("id") Long id){
        return categoryService.categoryDetailById(id);
    }
}
