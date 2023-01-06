package com.yyds.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javamenc.entity.PageResult;
import com.javamenc.entity.QueryPageBean;
import com.javamenc.pojo.Permission;
import com.javamenc.pojo.Role;
import com.javamenc.pojo.User;
import com.yyds.Service.UserService;
import com.yyds.dao.PermissionDao;
import com.yyds.dao.RoleDao;
import com.yyds.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Transactional
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<User> page= userDao.findByCondition(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Integer> findRolesByUserId(Integer userId) {
        return userDao.findRolesByUserId(userId);
    }

    @Override
    public void updateRoleAndUserId(Integer userId, Integer[] roleIds) {
        userDao.deleteAssocication(userId);
        // 重新建立用户所对应的角色的对应关系
        if (roleIds != null && roleIds.length > 0) {
            Map<String, Integer> map = null;
            for (Integer roleId : roleIds) {
                map = new HashMap<>();
                map.put("userId", userId);
                map.put("roleId", roleId);
                userDao.setUserAndRole(map);
            }
        }

    }

    @Override
    public User login(Map<String,String> map) {
        return userDao.selectUserByUsernameAndPassword(map);
    }

    @Override
    public User findByUserName(String username) {
        // 不使用 mybatis的级联查询
        // 先查询用户 --> 查询角色 --> 查询权限

        User user = userDao.findByUsername(username);
        if(user == null){
            return null;
        }

        // 根据用户id 查询所拥有的角色
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        if(roles != null && roles.size() > 0){
            for (Role role : roles) {
                // 根据角色查询相关的权限
                Integer roleId = role.getId();
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);

                if(permissions != null && permissions.size() > 0){
                    role.setPermissions(permissions);   // 设置查询到的角色所拥有的权限
                }
            }
            user.setRoles(roles);       // 设置查询到的用户所拥有的角色
        }
        return user;
    }



}
