package com.zijing.crm.settings.web.controller;

import com.zijing.crm.settings.domin.User;
import com.zijing.crm.settings.service.UserService;
import com.zijing.crm.settings.service.impl.UserServiceImpl;
import com.zijing.crm.utils.MD5Util;
import com.zijing.crm.utils.PrintJson;
import com.zijing.crm.utils.ServiceFactory;
import com.zijing.crm.utils.SqlSessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path = request.getServletPath();
        if ("/settings/user/login.do".equals(path)){
                login(request,response);
        }else if ("/settings/user/name.do".equals(path)){

        }

    }

    //登录方法
    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证登录操作");
        try {
            //设置字符集
            request.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //接收参数，并将密码进行MD5加密
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器的IP地址
        String ip = request.getRemoteAddr();
        System.out.println("==========================ip:"+ip);

        //获取代理对象，通过代理对象实现登录操作
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
       try {
           User user =  us.login(loginAct,loginPwd,ip);
           //如果程序走到这里说明登录成功
           request.getSession().setAttribute("user",user);

           /*当登录成功我们应该返回
            {"success":true}
           * */
           //调用PrintJson将结果以json格式返回
           PrintJson.printJsonFlag(response,true);
       }catch (Exception e){
           e.printStackTrace();

           //一旦程序执行到这，表明业务层为我们的验证失败，为controller抛出异常
           //表示登录失败

           /*
           * 登录失败返回
           * {"success":false,"msg" : "错误信息"}
           * */

           //业务层返回msg信息
           String msg = e.getMessage();
           /*
           * 我们现在作为controller，需要为ajax提供多项提示信息
           *
           * 可以有两种手段来处理
           *        1、将多项信息打包成map，将map解析为json串
           *        2、创建一共Vo类来存放数据
           *            private boolean success;
           *            private String  msg;
           *
           * 如果对于展现的信息将来还会大量使用，我们就创建一共Vo类
           * 如果对于展现的信息只有在这个需求能够使用，我们使用map就可以了
           * */
           Map map = new HashMap<String,Object>();
           map.put("success",false);
           map.put("msg",msg);
           PrintJson.printJsonObj(response,map);
       }

    }


}
