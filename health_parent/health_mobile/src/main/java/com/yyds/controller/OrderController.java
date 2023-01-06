package com.yyds.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;

import com.javamenc.constant.MessageConstant;
import com.javamenc.constant.RedisConstant;
import com.javamenc.entity.Result;
import com.yyds.Service.MapService;
import com.yyds.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private MapService mapService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        try {
            //1 按照用户的手机号码从redis中获取之前发送的验证码，和用户提交的验证码进行比较
            String telephone = (String) map.get("telephone");
            String code = jedisPool.getResource().get(telephone + "-" + RedisConstant.SENDTYPE_ORDER);
            //1.1 不一致，则返回错误信息
            if (StringUtils.isEmpty(code)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            String validateCode = map.get("validateCode").toString();
            if (!StringUtils.equals(code, validateCode)) {
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }
            map.put("orderType", "手机预约");
            map.put("orderStatus", "未到诊");
            //2 调用servic中的方法预约

            return orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map order = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/getAddressNames")
    public Result getAddressNames(){
        try {
            List<String> addressNames = mapService.getAddressNames();
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,addressNames);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
