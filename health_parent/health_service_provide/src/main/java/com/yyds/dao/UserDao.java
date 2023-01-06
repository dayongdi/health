package com.yyds.dao;

import com.github.pagehelper.Page;
import com.javamenc.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    //根据用户名和密码获取指定的账号
    public User selectUserByUsernameAndPassword(Map<String,String> map);


    // 根据用户名查询用户信息
    public User findByUsername(String username);

    // 条件查询
    public Page<User> findByCondition(String queryString);

    // 根据用户id 查询关联的角色
    public List<Integer> findRolesByUserId(Integer roleId);

    // 删除用户所对应的角色
    public void deleteAssocication(Integer userId);

    // 删除用户所对应的角色
    public void setUserAndRole(Map<String, Integer> map);

    // 根据用户名查找用户的个数
    public int findUserCountByUsername(String username);

    // 添加用户
    public void add(User user);
}
