package com.yyds.dao;

import com.github.pagehelper.Page;
import com.javamenc.pojo.CheckGroup;
import com.javamenc.pojo.Setmeal;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface SetMealDao {
    public Page<Setmeal> findPage(String queryString);

    public void add(Setmeal setmeal);

    public void update(Setmeal setmeal);

    public void delete(Integer setmealId);

    public Integer[] findCheckGroupIdS(Integer setmealId);

    public void setSetmealAndCheckGroup(Map<String,Integer> map);

    public Integer selectCountAssociationById(Integer setmealId);

    public void deleteAssociationById(Integer setmealId);

    public Setmeal findById(Integer id);

    public List<Setmeal> selectAll();

    //查询每个套餐所占比例
    public List<Map<String,Object>> findSetmealCount();


}
