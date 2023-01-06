package com.yyds.dao;

import com.javamenc.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {


    public void add(OrderSetting orderSetting);

    public void editNumberByOrderDate(OrderSetting orderSetting);

    public void editReservationsByOrderDate(OrderSetting orderSetting);

    public OrderSetting selectOrderSettingByOrderDate(Date orderDate);

    public List<OrderSetting> selectOrderSettingByMonth(Map<String,Date> map);

    public Integer selectOrderDateIsExist(Date orderDate);
}
