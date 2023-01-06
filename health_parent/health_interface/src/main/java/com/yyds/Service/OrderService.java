package com.yyds.Service;

import com.javamenc.entity.Result;

import java.util.Map;

public interface OrderService {
     // 进行预约的业务处理
     public Result order(Map map) throws Exception;

     // 根据预约信息id   查询一系列相关的数据  套餐信息  会员信息
     public Map findById(Integer id) throws Exception;
}
