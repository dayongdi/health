package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.javamenc.pojo.OrderSetting;
import com.javamenc.utils.DateUtils;
import com.yyds.Service.OrderSettingService;
import com.yyds.dao.OrderSettingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



//开启事务注解一定要明确Service对应接口
@Service(interfaceClass =OrderSettingService.class)
@Transactional
public class OrderSettingServiceImp implements OrderSettingService {



    @Autowired
    private OrderSettingDao orderSettingDao;


    //单独插入
    @Override
    public void insertOrderByDate(OrderSetting orderSetting) {
        //查询该日期是否已经在数据库中有数据，若有则进行更新操作
        Date orderDate = orderSetting.getOrderDate();
        Integer count = orderSettingDao.selectOrderDateIsExist(orderDate);
        if (count>0){
            //执行更新
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else{
            orderSettingDao.add(orderSetting);
        }
    }

    //批量导入
    @Override
    public void add(List<OrderSetting> list) {
        if (list!=null &&list.size()>0){
            for (OrderSetting orderSetting :list) {
                this.insertOrderByDate(orderSetting);
            }
        }
    }


    //用Map可以保证Key不重复.
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        // 2020-10-1  < ? < 2020-10-31
        // between...and...
        String beginString=date+"-1";
        String endString=date+"-31";

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date begin = null;
        Date end   =null;

        try {
            begin = dateFormat.parse(beginString);
            end =dateFormat.parse(endString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String,Date> map=new HashMap<String,Date>();
        map.put("begin",begin);
        map.put("end",end);

        List<OrderSetting> list = orderSettingDao.selectOrderSettingByMonth(map);

        List<Map> result=new ArrayList<>();
        //将数据从LIST写入Map
        if (list!=null&&list.size()>0){
            for (OrderSetting orderSetting:list) {
                Map<String,Object> m=new HashMap<>();
                m.put("date",orderSetting.getOrderDate().getDate());
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }


    //编辑某天的预约信息
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        this.insertOrderByDate(orderSetting);
    }
}
