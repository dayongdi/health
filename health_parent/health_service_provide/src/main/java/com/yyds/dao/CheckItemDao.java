package com.yyds.dao;

import com.github.pagehelper.Page;
import com.javamenc.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    public void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition(String queryString);

    public long findCountByCheckItem(Integer id);

    public void deleteById(Integer id);

    public CheckItem selectById(Integer id);

    public void update(CheckItem checkItem);

    public List<CheckItem> findAll();
}
