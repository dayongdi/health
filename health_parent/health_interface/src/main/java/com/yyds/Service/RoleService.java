package com.yyds.Service;

import com.javamenc.pojo.Role;

import java.util.List;

public interface RoleService {

    // 查询所有的角色数据
    public List<Role> findAll();

    // 根据角色id 查询包含的权限ids
    public List<Integer> findPermissionsByRoleId(Integer roleId);

    // 更新角色所对应的权限
    public void updatePermissionAndRoleId(Integer roleId, Integer[] permissionIds);
}
