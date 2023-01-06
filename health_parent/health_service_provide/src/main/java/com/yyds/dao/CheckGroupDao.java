package com.yyds.dao;

import com.github.pagehelper.Page;
import com.javamenc.pojo.CheckGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    public Page<CheckGroup> findPage(String parma);

    public void delete(Integer id);

    public void update(CheckGroup checkGroup);

    public CheckGroup findById(Integer id);

    public void add(CheckGroup checkGroup);

    public List<Integer> findCheckitemId(Integer id);

    public void setCheckGroupAndCheckItem(Map<String, Integer> map);

    public void deleteAssocication(Integer GroupId);

    public Integer selectAssociationById(Integer GroupId);

    public Integer findCountByCheckGroup(Integer id);

    public List<CheckGroup> findAll();
}
