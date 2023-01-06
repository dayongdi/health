package com.yyds.Service;

import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.Permission;

import java.util.List;

public interface PermissionService {
    // 分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean);

    // 添加一个新权限
    public void add(Permission permission);

    // 查询所有的权限数据
    public List<Permission> findAll();

}
