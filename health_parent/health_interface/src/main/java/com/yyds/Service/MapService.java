package com.yyds.Service;

import com.github.pagehelper.Page;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.Address;
import org.apache.ibatis.plugin.Intercepts;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface MapService {

    // 地图数据的分布查询
    public PageResult pageQuery(QueryPageBean queryPageBean);

    // 删除一个地址
    public void delete(Integer id);

    // 添加一个新地址
    public void add(Address address);

    // 获取公司地址的详细信息
    public List<Address> getAddressDetail();

    // 获取所有地址的名称
    public List<String> getAddressNames();
}
