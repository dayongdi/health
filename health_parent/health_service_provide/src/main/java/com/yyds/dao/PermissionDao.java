package com.yyds.dao;

import com.github.pagehelper.Page;
import com.javamenc.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    // 根据角色查询相关的权限
    public Set<Permission> findByRoleId(Integer roleId);

    // 利用条件查询数据
    public Page<Permission> findByCondition(String queryString);

    // 添加一个新权限
    public void add(Permission permission);

    // 查询所有的权限数据
    public List<Permission> findAll();

}
