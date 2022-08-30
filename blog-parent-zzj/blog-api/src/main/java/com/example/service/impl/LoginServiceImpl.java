package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.dao.pojo.SysUser;
import com.example.service.LoginService;
import com.example.utils.JWTUtils;
import com.example.vo.ErrorCode;
import com.example.vo.Result;
import com.example.vo.params.LoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final String slat = "mszlu!@#"; //加密盐 用于加密password 这里写死，也可以写在数据库里，每个用户的加密盐不一样

    @Autowired
    private SysUserServiceImpl sysUserService;  // 用来查用户表

    @Autowired
    private RedisTemplate<String,String> redisTemplate; // redis工具类


    @Override
    public Result login(LoginParams loginParams) {
        /**
         * 1、检查参数是否合法
         * 2、根据用户名和密码去user表中查询是否存在
         * 3、如果不存在  登录失败
         * 4、如果存在   使用jwt生成token    返回给前端
         * 5、token放入redis中，redis token：user 信息 设置过期时间 （登录认证时，先认证token字符串是否合法，后去redis中认证是否存在）
         */
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        //1
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //2
        password = DigestUtils.md5Hex(password + slat);
        SysUser sysUser = sysUserService.findUser(account,password);
        //3
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        //4
        String token = JWTUtils.createToken(sysUser.getId());

        //5
        //将sysUser转换成string存入redis并设置过期时间
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        return Result.success(token);
    }


    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isEmpty(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null ){
            return null;
        }

        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (userJson ==null){
            return null;
        }

        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParams loginParams) {
        /**
         * 1、判断参数是否合法
         * 2、判断账户是否存在   存在  返回账户已经被注册
         * 3、不存在    注册用户
         * 4、生成token
         * 5、存入redis    返回
         * 6、注意 加上事务，一旦中间出现任何问题 回滚事务
         */

        String account = loginParams.getAccount();
        String password = loginParams.getPassword();
        String nickname = loginParams.getNickname();
        if (StringUtils.isEmpty(account)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        SysUser sysUser = this.sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //注册储存
        SysUser sysUser2 = new SysUser();
        sysUser2.setNickname(nickname);
        sysUser2.setAccount(account);
        sysUser2.setPassword(DigestUtils.md5Hex(password + slat));
        sysUser2.setCreateDate(System.currentTimeMillis());
        sysUser2.setLastLogin(System.currentTimeMillis());
        sysUser2.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser2.setAdmin(1); //1 为true
        sysUser2.setDeleted(0); // 0 为false
        sysUser2.setSalt("");
        sysUser2.setStatus("");
        sysUser2.setEmail("");
        this.sysUserService.save(sysUser2);

        //token
        String token = JWTUtils.createToken(sysUser2.getId());


        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser2),1, TimeUnit.DAYS);
        return Result.success(token);

    }
}
