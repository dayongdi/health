package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Role;
import com.yyds.Service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleController {

    @Reference
    private RoleService roleService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Role> list = roleService.findAll();
            return new Result(true, MessageConstant.GET_ROLE_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLE_FAIL);
        }
    }

    // 根据角色id 查询包含的权限ids
    @GetMapping("/findPermissionsByRoleId")
    public Result getPermissionIds(Integer roleId){
        try{
            List<Integer> permissionIds = roleService.findPermissionsByRoleId(roleId);
            return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS, permissionIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_PERMISSION_FAIL);
        }
    }

    // 更新角色所对应的权限
    @PutMapping("/updatePermissionAndRoleId")
    public Result updatePermissionAndRoleId(Integer roleId,@RequestBody Integer[] permissionIds){
        try{
            roleService.updatePermissionAndRoleId(roleId, permissionIds);
            return new Result(true, MessageConstant.SET_PERMISSION_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SET_PERMISSION_FAIL);
        }
    }
}
