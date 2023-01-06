package com.yyds.Service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Order;
import com.javamenc.pojo.OrderSetting;
import com.javamenc.utils.DateUtils;
import com.yyds.Service.OrderSettingListService;
import com.yyds.dao.OrderSettingDao;
import com.yyds.dao.OrderSettingListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Transactional
@Service(interfaceClass = OrderSettingListService.class)
public class OrderSettingListServiceImp implements OrderSettingListService {

    @Autowired
    private OrderSettingListDao orderSettingListDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {

        //利用分页工具
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        Page<Map> page=orderSettingListDao.findByCondition(queryPageBean.getQueryString());


        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void updateStatusByOrderId(int orderId) throws Exception {
        // 判断是否已经预约
        Order byOrderId = orderSettingListDao.findByOrderId(orderId);
        if(byOrderId != null && !Order.ORDERSTATUS_YES.equals(byOrderId.getOrderStatus())){
            // 更新预约状态
            orderSettingListDao.updateStatusByOrderId(orderId);
        }else{
            throw new Exception("抛出异常");        // 修改失败（不存在、已经预约）父方法捕获
        }
    }


    @Override
    public void delete(Map map) throws Exception {
        //判断是否已经到诊过,不是则可以取消预约
        if (map.get("orderStatus").equals(Order.ORDERSTATUS_YES)){
            throw new Exception("已经到诊过，无法取消预约");      // 修改失败（不存在、已经预约）父方法捕获
        }

        //根据预约日期获取预约日期的对象
        OrderSetting orderSetting = orderSettingDao.selectOrderSettingByOrderDate(DateUtils.parseString2Date((String)map.get("orderDate")));

        // 修改预约日期的人数 -1
        orderSetting.setReservations(orderSetting.getReservations() - 1);
        //将数据写回数据库
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //删除预约信息
        orderSettingListDao.deleteByOrderId((Integer) map.get("id"));
    }
}
