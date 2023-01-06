package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.CheckItem;
import com.yyds.Service.CheckItemService;
import com.yyds.dao.CheckItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//开启事务注解一定要明确Service对应借口
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    //通过自动扫描注入Dao对象
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //解析数据
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize =queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //利用MyBatis框架提供的分页查询工具查询
        PageHelper.startPage(currentPage,pageSize);

        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();

        return new PageResult(total,rows);
    }

    @Override
    public void deleteById(Integer id) {
        long count = checkItemDao.findCountByCheckItem(id);
        if (count>0){
            throw new RuntimeException("改检查项目关联在项目组中，无法直接删除");
        }
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findCheckItemById(Integer id) {
        return checkItemDao.selectById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
