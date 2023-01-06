package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Order;
import com.javamenc.utils.SMSUtils;
import com.yyds.Service.OrderService;
import com.yyds.Service.OrderSettingListService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


//检查项管理
//RestController包含了Controller还会自动帮我们把数据转为Json格式返回
@RestController
@RequestMapping("/orderSettingList")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEALTH_MANAGER')")
public class OrderSettingListController {

    @Reference
    private OrderSettingListService orderSettingListService;


    @Reference
    private OrderService orderService;

    // 日期格式化
    public static String formatDateStr(String dateString){
        String[] ts = dateString.split("T")[0].split("-");
        int number = Integer.parseInt(ts[2]);
        ++number;
        ts[2] = String.valueOf(number);
        return ts[0] + "-" + ts[1] + "-" + ts[2];
    }

    @PostMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return orderSettingListService.pageQuery(queryPageBean);
    }

    @PostMapping("/cancelOrder")
    public Result cancelOrder(@RequestBody Map map){
        try {
            orderSettingListService.delete(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "已经到诊过，无法取消预约");
        }
        return new Result(true, MessageConstant.ORDER_CANCEL_SUCCESS);
    }

    @PutMapping("/updateStatus")
    public Result updateStatus(int orderId){
        try {
            orderSettingListService.updateStatusByOrderId(orderId);
            return new Result(true,MessageConstant.ORDER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.ORDER_FAIL);
    }

    @PostMapping("/add")
    public Result add(String setmealId, @RequestBody Map map){
        Result result = new Result(false, MessageConstant.ORDER_FAIL);
        try {
            map.put("setmealId",setmealId);

            //将日期对象强转为String
            String str =(String)map.get("orderDate");
            map.put("orderDate",formatDateStr(str));

            str =(String)map.get("birthday");
            map.put("birthday",formatDateStr(str));

            //进行预约业务
            // 进行预约的业务处理
            map.put("orderType", Order.ORDERTYPE_TELEPHONE);

            result = orderService.order(map);

        } catch (Exception e) {
            e.printStackTrace();

            return result;
        }

        if (result.isFlag()) {      // 预约成功  发送短信进行通知
            String orderDate = (String) map.get("orderDate");
            try {
                // 无法发送通知   只能用验证码代替
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, map.get("telephone").toString(), orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
