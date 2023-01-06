package com.yyds.dao;

import com.javamenc.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    // 根据用户id 查询所拥有的角色
    public Set<Role> findByUserId(Integer userId);

    // 查询所有的角色数据
    public List<Role> findAll();

    // 根据角色id 查询包含的权限ids
    public List<Integer> findPermissionsByRoleId(Integer roleId);

    // 删除角色所对应的权限关系
    public void deleteAssocication(Integer roleId);

    // 重新建立角色与权限的对应关系
    public void setPermissionAndRole(Map map);
}
