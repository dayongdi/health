package com.yyds.dao;

import com.javamenc.pojo.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    //插入预约信息
    public void add(Order oder);

    //动态条件查询
    public List<Order> findOrderByCondition(Order order);

    //根据预约id查询预约信息，包括体检人信息、套餐信息
    public Map findById4Detail(int id);

    //根据日期统计预约数
    public int selectOrderCountByDate(Date date);

    //根据日期统计预约数，统计指定日期之后的预约数
    public int selectOrderCountAfterDate(Date date);

    //根据日期统计到诊数
    public int selectVisitsCountByDate(Date date);

    //根据日期统计到诊数，统计指定日期之后的到诊数
    public int selectVisitsCountAfterDate(Date date);

    //热门套餐，查询前5条
    public List<Map> selectHotSetmeal();

}
