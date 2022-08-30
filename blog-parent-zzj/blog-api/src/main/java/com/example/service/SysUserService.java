package com.example.service;

import com.example.dao.pojo.SysUser;
import com.example.vo.Result;
import com.example.vo.UserVo;

public interface SysUserService {

    UserVo findUserByid(Long id);

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result finUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);


    /**
     *保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);
}
