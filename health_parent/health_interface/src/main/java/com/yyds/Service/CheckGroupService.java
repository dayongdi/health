package com.yyds.Service;

import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.CheckGroup;
import com.sun.jdi.IntegerType;

import java.util.List;

public interface CheckGroupService {

    public PageResult findPage(QueryPageBean queryPageBean);

    public CheckGroup findById(Integer id);

    public void deleteById(Integer id);

    public void edit(Integer[] checkitemIds,CheckGroup checkGroup);

    public void add(Integer[] checkitemIds, CheckGroup checkGroup);

    public List<Integer>  findCheckitemId(Integer id);

    public void setCheckGroupAndCheckItem(Integer chekGroupId, Integer[] checkItemIds);

    public List<CheckGroup> findAll();
}
