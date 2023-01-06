package com.yyds.Service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javamenc.constant.RedisConstant;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.CheckGroup;
import com.javamenc.pojo.CheckItem;
import com.javamenc.pojo.Setmeal;
import com.yyds.Service.SetMealService;
import com.yyds.dao.CheckGroupDao;
import com.yyds.dao.CheckItemDao;
import com.yyds.dao.SetMealDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    //自动代理
    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        //利用MYBATIS分页工具
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page=setMealDao.findPage(queryString);


        long total=page.getTotal();
        List<Setmeal> rows=page.getResult();


        return new PageResult(total,rows);
    }

    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.add(setmeal);
        setSetmealAndCheckGroup(setmeal.getId(),checkGroupIds);
        // 将保存到数据库中的文件名称保存到redis中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
    }

    @Override
    public void delete(Integer setmealId) {
        //查询是否拥有关系，先删关系再删除
        if (setMealDao.selectCountAssociationById(setmealId)>0){
            setMealDao.deleteAssociationById(setmealId);
        }
        setMealDao.delete(setmealId);

    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.update(setmeal);
        Integer setmealId=setmeal.getId();
        //删除关系
        if (setMealDao.selectCountAssociationById(setmealId)>0){
            setMealDao.deleteAssociationById(setmealId);
        }
        //重新建立关系
        setSetmealAndCheckGroup(setmealId,checkGroupIds);
    }

    @Override
    public Setmeal findById(Integer setmealId) {
        return  setMealDao.findById(setmealId);
    }

    @Override
    public Integer[] findCheckGroupIds(Integer setmealId) {
        return setMealDao.findCheckGroupIdS(setmealId);
    }

    @Override
    public List<Setmeal> findAll() {
        return setMealDao.selectAll();
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setMealDao.findSetmealCount();
    }

    @Override
    public Setmeal findByIdOnMobile(Integer setmealId) {
        Setmeal setmeal = setMealDao.findById(setmealId);
        Integer[] checkGroupIdS = setMealDao.findCheckGroupIdS(setmealId);
        List<CheckGroup> checkGroupList=new ArrayList<CheckGroup>();

        for (Integer checkGroupId :checkGroupIdS) {
            List<CheckItem> checkItemList=new ArrayList<CheckItem>();
            CheckGroup byId = checkGroupDao.findById(checkGroupId);
            List<Integer> checkitemIds = checkGroupDao.findCheckitemId(checkGroupId);
            for (Integer checkitemId:checkitemIds){
                CheckItem checkItem = checkItemDao.selectById(checkitemId);
                checkItemList.add(checkItem);
            }
            byId.setCheckItems(checkItemList);
            checkGroupList.add(byId);
        }
        setmeal.setCheckGroups(checkGroupList);
        return setmeal;
    }


    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkGroupIds){
        if (checkGroupIds !=null &&checkGroupIds.length>0){
            Map<String,Integer> map=null;
            for (Integer checkGroupId:checkGroupIds) {
                map= new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkGroupId",checkGroupId);
                setMealDao.setSetmealAndCheckGroup(map);
            }
        }
    }



}
