package com.yyds.Service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.Address;
import com.yyds.Service.MapService;
import com.yyds.dao.MapDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(interfaceClass = MapService.class)
public class MapServiceImpl implements MapService{

    @Autowired
    private MapDao mapDao;

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<Address> pages=mapDao.findByCondition(queryPageBean.getQueryString());

        return new PageResult(pages.getTotal(),pages.getResult());
    }

    @Override
    public void delete(Integer id) {
        mapDao.delete(id);
    }

    @Override
    // 添加一个新地址
    public void add(Address address) {
        mapDao.add(address);
    }

    @Override
    // 获取公司地址的详细信息
    public List<Address> getAddressDetail() {
        return mapDao.findAddressAll();
    }

    @Override
    public List<String> getAddressNames() {
        return mapDao.getAddressNames();
    }
}
