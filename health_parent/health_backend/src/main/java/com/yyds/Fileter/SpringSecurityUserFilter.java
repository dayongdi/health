package com.yyds.Fileter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.pojo.Permission;
import com.javamenc.pojo.Role;
import com.javamenc.pojo.User;


import com.yyds.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;


// 将此类配置到 spring容器中, 用于在 spring-security.xml中配置使用
public class SpringSecurityUserFilter implements UserDetailsService{

    @Reference
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        User user=userService.findByUserName(username);
        if(user == null){
            return null;
        }
        // 获取该用户所拥有的角色和权限
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if(roles != null){
            for (Role role : roles) {
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
        }

        //对密码进行加密
        UserDetails securityUser =new org.springframework.security.core.userdetails.User
                (username, passwordEncoder.encode(user.getPassword()), list);
        return securityUser;
    }



}