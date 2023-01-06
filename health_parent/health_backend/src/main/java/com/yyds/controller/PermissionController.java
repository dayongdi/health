package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Permission;
import com.yyds.Service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/permission")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<Permission> list =  permissionService.findAll();
            return new Result(true, MessageConstant.GET_PERMISSION_SUCCESS, list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_PERMISSION_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return permissionService.pageQuery(queryPageBean);
    }

    // 添加一个新权限
    @PostMapping("/add")
    public Result add(@RequestBody Permission permission){
        try{
            permissionService.add(permission);

            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }

}
