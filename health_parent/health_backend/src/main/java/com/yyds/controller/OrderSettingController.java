package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.Result;
import com.javamenc.pojo.OrderSetting;
import com.javamenc.utils.POIUtils;
import com.yyds.Service.OrderSettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//检查项管理
//RestController包含了Controller还会自动帮我们把数据转为Json格式返回
@RestController
@RequestMapping("/ordersetting")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEALTH_MANAGER')")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //用POI工具解析文件
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingList=new ArrayList<OrderSetting>();

            OrderSetting orderSetting=null;
            if (strings!=null){
                for (String[] string: strings) {
                    String orderDate= string[0];
                    String number=string[1];
                    orderSetting=new OrderSetting(new Date(orderDate),Integer.parseInt(number));
                    orderSettingList.add(orderSetting);
                }
                orderSettingService.add(orderSettingList);
            }
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    //获取整个月的预约信息
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch(Exception e){
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ORDERSETTING_FAIL);
    }


}
