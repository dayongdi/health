package com.yyds.Service;


import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    public void add(CheckItem checkItem);

    public PageResult findPage(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    public CheckItem findCheckItemById(Integer id);

    public void edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}
