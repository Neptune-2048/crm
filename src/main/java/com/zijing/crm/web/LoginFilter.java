package com.zijing.crm.web;

import com.zijing.crm.settings.domin.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //过滤没有进行登录的请求
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //获取访问的文件的路径
        //例如URL为 htttp://localhost:8080/crm/login.jsp
        //这时候下面的path获取的"/login.jsp"
        String path = request.getServletPath();
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            //不应该拦截访问登录页面以及登录页面所关联的文件
            chain.doFilter(req,resp);
            System.out.println(path+"====================++++++++++++++++++++++++++++");
        }else {

            //如果session中user为null则用户没有登录(只有登录成功了user才不会为null)
            User user = (User) request.getSession().getAttribute("user");

            if(user!=null){
                //进行了登录，并且登录成功，放行
                chain.doFilter(req,resp);
            }else {
                //重定向到登录页
                /*
                 *
                 *
                 * */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }

        }




    }
}
