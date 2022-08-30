package com.example.blogadmin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogadmin.mapper.PermissionMapper;
import com.example.blogadmin.model.params.PageParam;
import com.example.blogadmin.pojo.Permission;
import com.example.blogadmin.vo.PageResult;
import com.example.blogadmin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public Result listPermission(PageParam pageParam) {
        Page<Permission> page = new Page<>(pageParam.getCurrentPage(),pageParam.getPageSize());
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isEmpty(pageParam.getCurrentPage())){
            queryWrapper.eq(Permission::getName,pageParam.getQueryString());
        }
        IPage iPage = permissionMapper.selectPage(page, queryWrapper);
        PageResult<PageParam> pageResult = new PageResult<>();
        pageResult.setList(iPage.getRecords());
        pageResult.setTotal(iPage.getTotal());
        return Result.success(pageResult);
    }

    public Result add(Permission permission) {
        this.permissionMapper.insert(permission);
        return Result.success(null);
    }

    public Result update(Permission permission) {
        this.permissionMapper.updateById(permission);
        return Result.success(null);
    }

    public Result delete(Long id) {
        this.permissionMapper.deleteById(id);
        return Result.success(null);
    }
}
