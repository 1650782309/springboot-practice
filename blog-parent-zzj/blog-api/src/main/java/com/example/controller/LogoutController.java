package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.LoginService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
//    @LogAnnotation(module="注销",operation="注销用户")
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
