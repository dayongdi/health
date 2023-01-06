package com.yyds.Service;

import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.entity.Result;
import com.javamenc.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public void add(User user);

    public PageResult findPage(QueryPageBean queryPageBean);

    public List<Integer> findRolesByUserId(Integer userId);

    public void updateRoleAndUserId(Integer userId,Integer[] roleIds);

    public User login(Map<String,String> map);

    public User findByUserName(String username);

}
