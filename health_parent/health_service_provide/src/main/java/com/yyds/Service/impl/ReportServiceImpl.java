package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.javamenc.utils.DateUtils;
import com.yyds.Service.ReportService;
import com.yyds.dao.MemberDao;
import com.yyds.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        // 报表日期（今天的日期）
        Date today = DateUtils.getToday();
        // 获取本周一日期
        Date thisWeekMonday =DateUtils.getThisWeekMonday();
        // 获取本月第一天日期
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();


        // 获取会员相关的数据***
        // 本日新增的会员数
        Integer todayNewMember = memberDao.selectMemberCountByDate(today);
        // 总的会员数
        Integer totalMember = memberDao.selectMemberCount();
        // 本周新增的会员数
        Integer thisWeekNewMember = memberDao.selectMemberCountByDateAfter(thisWeekMonday);
        // 本月新增的会员数
        Integer thisMonthNewMember = memberDao.selectMemberCountByDateAfter(firstDay4ThisMonth);


        // 获取预约相关的数据***
        // 今日预约数
        Integer todayOrderNumber = orderDao.selectOrderCountByDate(today);
        // 本周预约数
        Integer thisWeekOrderNumber = orderDao.selectOrderCountAfterDate(thisWeekMonday);
        // 本月预约数
        Integer thisMonthOrderNumber = orderDao.selectOrderCountAfterDate(firstDay4ThisMonth);
        // 今日到诊数
        Integer todayVisitsNumber = orderDao.selectVisitsCountByDate(today);
        // 本周到诊数
        Integer thisWeekVisitsNumber = orderDao.selectVisitsCountAfterDate(thisWeekMonday);
        // 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.selectVisitsCountAfterDate(firstDay4ThisMonth);


        // 查询热门套餐的相关数据
        List<Map> hotSetmeal = orderDao.selectHotSetmeal();

        Map<String,Object> data=new HashMap<>();
        data.put("reportDate",today);
        data.put("todayNewMember",todayNewMember);
        data.put("totalMember",totalMember);
        data.put("thisWeekNewMember",thisWeekNewMember);
        data.put("thisMonthNewMember",thisMonthNewMember);
        data.put("todayOrderNumber",todayOrderNumber);
        data.put("thisWeekOrderNumber",thisWeekOrderNumber);
        data.put("thisMonthOrderNumber",thisMonthOrderNumber);
        data.put("todayVisitsNumber",todayVisitsNumber);
        data.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        data.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        data.put("hotSetmeal",hotSetmeal);

        return data;
    }


}
