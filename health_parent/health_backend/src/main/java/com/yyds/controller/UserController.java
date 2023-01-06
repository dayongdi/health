package com.yyds.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.User;
import com.yyds.Service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    @Reference
    private UserService userService;

    @PostMapping("/add")

    public Result add(@RequestBody User user){
        try {
            userService.add(user);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return userService.findPage(queryPageBean);
    }



    // 根据用户id 查询关联的角色
    @GetMapping("/findRolesByUserId")
    public Result findRolesByUserId(Integer userId){
        try{
            List<Integer> roleIds = userService.findRolesByUserId(userId);
            return new Result(true, MessageConstant.GET_ROLE_SUCCESS, roleIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ROLE_FAIL);
        }
    }

    // 更新用户所对应的角色
    @PutMapping("/updateRoleAndUserId")
    public Result updateRoleAndUserId(Integer userId,@RequestBody Integer[] roleIds){
        try{
            userService.updateRoleAndUserId(userId, roleIds);

            return new Result(true, MessageConstant.SET_ROLE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SET_ROLE_FAIL);
        }
    }

    @GetMapping("/getUserName")
    public Result getUserName(HttpServletRequest request){

        // 使用 springsecurity封装中的框架中获取session中存储的用户信息
        org.springframework.security.core.userdetails.User user=
                (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        request.getSession().getAttribute("username");
        if(user != null){
            // 将当前认证的用户名返回到前台界面中
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
        }

        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }



}
