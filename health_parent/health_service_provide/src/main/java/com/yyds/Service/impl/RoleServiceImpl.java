package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.javamenc.pojo.Role;
import com.yyds.Service.RoleService;
import com.yyds.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;


    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public List<Integer> findPermissionsByRoleId(Integer roleId) {
        return roleDao.findPermissionsByRoleId(roleId);
    }

    @Override
    public void updatePermissionAndRoleId(Integer roleId, Integer[] permissionIds) {
        //删除角色的所有权限
        roleDao.deleteAssocication(roleId);
        //新增角色的权限
        if (permissionIds!=null &&permissionIds.length>0){
            Map<String,Integer> map=null;
            for (Integer permission: permissionIds) {
                map=new HashMap<>();
                map.put("roleId",roleId);
                map.put("permissionId",permission);
                roleDao.setPermissionAndRole(map);
            }
        }
    }
}
