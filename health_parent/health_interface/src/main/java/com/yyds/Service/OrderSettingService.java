package com.yyds.Service;

import com.javamenc.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    public void insertOrderByDate(OrderSetting orderSetting);

    // 数据批量导入到数据库
    public void add(List<OrderSetting> list);

    // 根据月份查询对应的预约设置数据
    public List<Map> getOrderSettingByMonth(String date);

    // 编辑指定的日期预约设置
    public void editNumberByDate(OrderSetting orderSetting);
}
