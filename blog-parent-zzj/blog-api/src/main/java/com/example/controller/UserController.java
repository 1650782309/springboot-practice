package com.example.controller;

import com.example.common.aop.LogAnnotation;
import com.example.service.SysUserService;
import com.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
//    @LogAnnotation(module="用户",operation="当前登录用户管理")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.finUserByToken(token);

    }

}
