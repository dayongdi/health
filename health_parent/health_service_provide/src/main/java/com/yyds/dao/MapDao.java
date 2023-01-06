package com.yyds.dao;

import com.github.pagehelper.Page;
import com.javamenc.pojo.Address;

import java.util.List;

public interface MapDao {
    // 条件查询
    public Page<Address> findByCondition(String queryString);

    // 删除一个地址
    public void delete(Integer id);

    // 添加一个新地址
    public void add(Address address);

    // 获取公司地址的详细信息
    public List<Address> findAddressAll();

    // 获取所有地址的名称
    public List<String> getAddressNames();

    // 根据公司地址名称查询个数
    int findCountByAddressName(String companyAddress);


}
