package com.zijing.crm.web;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
        //过滤器,将字符编码与解码都改为utf-8
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //过滤post请求的中文乱码
        req.setCharacterEncoding("utf-8");
        //过滤响应流的响应乱码
        resp.setContentType("text/html;charset=utf-8");
        //将请求和响应放行
        chain.doFilter(req,resp);
    }
}
