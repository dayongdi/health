package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.CheckGroup;
import com.yyds.Service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



//@PreAuthorize("hasAuthority('USER_QUERY')")//表示用户必须拥有USER_QUERY权限才能调用当前方法

//检查项管理
//RestController包含了Controller还会自动帮我们把数据转为Json格式返回
@RestController
@RequestMapping("/checkGroup")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEALTH_MANAGER')")
public class CheckGroupController {

    //通知Dubbo寻找服务
    @Reference
    private CheckGroupService checkGroupService;


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            return checkGroupService.findPage(queryPageBean);
    }

    //根据Id查数据
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return  new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try{
            checkGroupService.edit(checkitemIds,checkGroup);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            checkGroupService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(Integer[] checkitemIds,@RequestBody CheckGroup checkGroup){
        try{
            checkGroupService.add(checkitemIds,checkGroup);
            return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("/findCheckitemIdByCheckGroupId")
    public Result findCheckitemIdByCheckGroupId(Integer id){
        try{
            List<Integer> list=checkGroupService.findCheckitemId(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<CheckGroup> checkGroups=checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


}
