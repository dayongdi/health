package com.yyds.Service;

import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    public PageResult findPage(QueryPageBean queryPageBean);

    public void add(Setmeal setmeal,Integer[] CheckGroupIds);

    public void delete(Integer setmealId);

    public void edit(Setmeal setmeal, Integer[] checkGroupIds);


    public Setmeal findById(Integer setmealId);


    public Integer[] findCheckGroupIds(Integer setmealId);

    public List<Setmeal> findAll();


    // 查询每个套餐对应的数量
    public List<Map<String,Object>> findSetmealCount();

    //用于查询相关套餐和组合多项数据用于mobile
    public Setmeal findByIdOnMobile(Integer setmealId);

}
