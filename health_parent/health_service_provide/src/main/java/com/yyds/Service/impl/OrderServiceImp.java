package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Member;
import com.javamenc.pojo.Order;
import com.javamenc.pojo.OrderSetting;
import com.javamenc.utils.DateUtils;
import com.yyds.Service.OrderService;
import com.yyds.dao.OrderDao;
import com.yyds.dao.OrderSettingDao;
import com.yyds.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

//用户还未进行地址验证 待后续完善

@Transactional
@Service(interfaceClass = OrderService.class)
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    //进行预约
    @Override
    public Result order(Map map) throws Exception {
        // 判断预约的地址是否是正确的  返回的公司地址对应的id
        int addressId=1;
        if ( map.get("addressId")!=null){
            addressId=Integer.parseInt((String) map.get("addressId") );
        }

        // 检查用户的预约日期是否已经进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);      // 指定日期的格式

        OrderSetting orderSetting = orderSettingDao.selectOrderSettingByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        // 检查用户的预约日期是否已经约满
        int number = orderSetting.getNumber();              // 可预约人数
        int reservations = orderSetting.getReservations();  // 已预约人数
        if (reservations >= number) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        // 检查用户是否重复预约（同一个用户在同一天预约了同一个套餐）
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId, date, null, null, setmealId, 0);
            List<Order> list = orderDao.findOrderByCondition(order);
            if (list != null && list.size() > 0) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        // 检查用户是否为会员，如果不是则自动完成注册 t_member 并进行预约
        if (member == null) {
            // 封装会员的信息进行保存
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            member.setBirthday(DateUtils.parseString2Date((String)map.get("birthday")));
            member.setMaritalStatus((String)map.get("maritalStatus"));
            memberDao.add(member);
        }

        // 保存预约信息到预约表
        Order order=new Order(member.getId(),date,(String)map.get("orderType"),Order.ORDERSTATUS_NO,
                Integer.parseInt((String) map.get("setmealId")), addressId);


        orderDao.add(order);
        // 设置已预约人数 + 1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        //预约成功并返回预约序号
        return  new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {
        return orderDao.findById4Detail(id);
    }
}
