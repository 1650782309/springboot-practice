package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.LoginService;
import com.example.vo.Result;
import com.example.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private LoginService loginService;

    @PostMapping
//    @LogAnnotation(module="注册",operation="注册账户")
    public Result register(@RequestBody LoginParams loginParams){
        return loginService.register(loginParams);
    }
}
