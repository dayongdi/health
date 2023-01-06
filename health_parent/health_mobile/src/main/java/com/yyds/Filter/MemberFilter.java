package com.yyds.Filter;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.pojo.Member;
import com.yyds.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;




public class MemberFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException {
        //将父接口转为子接口
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) rep;

        String uri = request.getRequestURI();
        String substring = uri.substring(uri.lastIndexOf("/") + 1);
        System.out.println(substring);

        //如果是首页或登录页就不过滤
        if (substring.equals("login.html") || substring.equals("index.html")){
            chain.doFilter(request,response);
            return;
        }else{
            //除此之外，没有登录自动跳转到登录页,若登录则继续
            Member member =(Member) request.getSession().getAttribute("member");
            if (member == null ){
                response.sendRedirect(request.getContextPath()+"/pages/login.html");
                return;
            }
            chain.doFilter(request,response);
        }

    }

    @Override
    public void destroy() {

    }
}
