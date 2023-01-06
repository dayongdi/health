package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Setmeal;
import com.yyds.Service.CheckGroupService;
import com.yyds.Service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetMealService setMealService;

    @Reference
    private CheckGroupService checkGroupService;

    @Autowired
    private JedisPool jedisPool;

    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        //优先从缓存中获取，没有再从数据库查询
        String allSetmealJson = jedisPool.getResource().get("allSetmeal");
        try {
            if (allSetmealJson ==null){
                List<Setmeal> setmeals = setMealService.findAll();
                //将数据存入redis缓存中
                String setmealsJson= JSON.toJSONString(setmeals);
                jedisPool.getResource().setex("allSetmeal",60*60,setmealsJson); //缓存保存1小时
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeals);
            }else{
                //缓存中有数据，将数据转换为list数据再传给页面
                JSONArray allSetmealText = JSONArray.parseArray(allSetmealJson);
                List<Setmeal> setmeals = JSONObject.parseArray(allSetmealText.toJSONString(), Setmeal.class);
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeals);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


    //查询关于套餐的所有信息
    @RequestMapping("/findByIdOnMobile")
    public Result findById(Integer id) {
        try {
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setMealService.findByIdOnMobile(id));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //只查询套餐不包含里面的项目组和项目信息
    @RequestMapping("/findByIdBasic")
    public Result findByIdBasic(Integer id) {
        try {
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setMealService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }


}
