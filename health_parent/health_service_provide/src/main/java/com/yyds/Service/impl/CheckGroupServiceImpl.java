package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.CheckGroup;
import com.yyds.Service.CheckGroupService;
import com.yyds.dao.CheckGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//开启事务注解一定要明确Service对应借口
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService
{
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //解析数据
        Integer currentPage= queryPageBean.getCurrentPage();
        Integer pageSize= queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        //利用MyBatis框架提供的分页查询工具查询
        PageHelper.startPage(currentPage,pageSize);

        Page<CheckGroup> page=checkGroupDao.findPage(queryString);
        long total=page.getTotal();

        List<CheckGroup> rows=page.getResult();

        return new PageResult(total,rows);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public void deleteById(Integer id) {
        //判断是检查组是否包含在套餐关系中
        if(checkGroupDao.findCountByCheckGroup(id)>0){
            throw new RuntimeException("有套餐引用了该检查组，无法直接删除");
        }
        if(checkGroupDao.selectAssociationById(id)>0){
            //优先删除关系
            checkGroupDao.deleteAssocication(id);
        }

        checkGroupDao.delete(id);
    }

    @Override
    public void edit(Integer[] checkitemIds,CheckGroup checkGroup) {
        checkGroupDao.update(checkGroup);
        //删除原本关系

        if(checkGroupDao.selectAssociationById(checkGroup.getId())>0){
            //优先删除关系
            checkGroupDao.deleteAssocication(checkGroup.getId());
        }
        //重新建立关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public List<Integer> findCheckitemId(Integer id) {
        List <Integer> list=checkGroupDao.findCheckitemId(id);
        return list;
    }

    @Override
    public void setCheckGroupAndCheckItem(Integer chekGroupId, Integer[] checkItemIds) {
        if (checkItemIds !=null &&checkItemIds.length>0){
            Map<String,Integer> map=null;
            for (Integer checkItemId:checkItemIds){
                map= new HashMap<>();
                map.put("checkGroup_id",chekGroupId);
                map.put("checkItem_id",checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public List<CheckGroup> findAll(){
        return checkGroupDao.findAll();
    }

}
