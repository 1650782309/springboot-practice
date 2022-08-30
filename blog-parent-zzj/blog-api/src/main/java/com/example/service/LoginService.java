package com.example.service;

import com.example.dao.pojo.SysUser;
import com.example.vo.Result;
import com.example.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;


public interface LoginService {
    /**
     * 登录功能
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    SysUser checkToken(String token);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     * @param loginParams
     * @return
     */
    Result register(LoginParams loginParams);
}
