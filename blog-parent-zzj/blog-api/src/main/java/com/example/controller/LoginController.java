package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.LoginService;
import com.example.vo.Result;
import com.example.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
//    @LogAnnotation(module="登录",operation="登录账户")
    public Result login(@RequestBody LoginParams loginParams){
        //登录 验证用户 访问用户表
        return loginService.login(loginParams);
    }

}
