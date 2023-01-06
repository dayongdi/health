// package com.yyds.Fileter;
//
//
// import javax.servlet.*;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
//


//自定义的登录过滤器已经弃用，改用spring-security的权限框架






// public class UserFilter implements Filter {
//
//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//
//     }
//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//         //httpServletRequest是ServletRequest的子类，可以强转，并且提供了一些GetSession方法等
//         HttpServletRequest httpServletRequest= (HttpServletRequest) request;
//         HttpServletResponse httpServletResponse= (HttpServletResponse) response;
//         Object user = httpServletRequest.getSession().getAttribute("user");
//
//         String uri = httpServletRequest.getRequestURI();
//         String substring = uri.substring(uri.lastIndexOf("/") + 1);
//         System.out.println(substring);
//
//
//         if (user==null && !substring.equals("login.html")){
//             httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/pages/login.html");
//         }else{
//             chain.doFilter(request,response);
//         }
//     }
//
//     @Override
//     public void destroy() {
//
//     }
// }
