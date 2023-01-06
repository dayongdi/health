package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.constant.RedisConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Setmeal;
import com.javamenc.utils.AliYunOssUtils;
import com.yyds.Service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/setmeal")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEALTH_MANAGER')")
public class SetMealController {

    //通知Dubbo寻找服务
    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setMealService.add(setmeal,checkGroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_SUCCESS);
        }
    }

    @RequestMapping("/deleteById")
    public Result deleteById(Integer setMealId){
        try {
            setMealService.delete(setMealId);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setMealService.findPage(queryPageBean);
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setMealService.edit(setmeal,checkGroupIds);
            return new Result(true, MessageConstant.EDIT_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer setmealId){
        try {
            Setmeal setmeal=setMealService.findById(setmealId);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


    @RequestMapping("/findGroupIds")
    public Result findGroupIds(Integer setmealId){
        try {
            Integer[] checkGroupIds=setMealService.findCheckGroupIds(setmealId);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile){
        String originalFilename=imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        System.out.println(index);
        //获取扩展名
        String extention=originalFilename.substring(index);

        System.out.println(extention);
        String filename= UUID.randomUUID().toString()+extention;

        //利用工具类上传至阿里云
        try {
            AliYunOssUtils.uploadFile(imgFile.getBytes(),filename);
            //将名字储存到jedis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
    }


    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Setmeal> list=setMealService.findAll();
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
    }

}
