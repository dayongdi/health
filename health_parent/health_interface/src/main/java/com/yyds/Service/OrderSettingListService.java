package com.yyds.Service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface OrderSettingListService {
    // 分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean);

    // 修改预约的状态
    public void updateStatusByOrderId(int orderId) throws Exception;


    // 删除一条预约信息
    public void delete(Map map) throws Exception;
}
