package com.zijing.crm.settings.service.impl;

import com.zijing.crm.exception.LoginException;
import com.zijing.crm.settings.dao.UserDao;
import com.zijing.crm.settings.domin.User;
import com.zijing.crm.settings.service.UserService;
import com.zijing.crm.utils.DateTimeUtil;
import com.zijing.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);//报错，无法初始化？？？？？

    public User login(String loginAct, String loginPwd, String ip) throws LoginException{
        //业务层处理登录操作
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        //调用Dao层
        User user = userDao.login(map);

        if (user==null){
            throw new LoginException("账号密码错误");
        }

        //如果能执行到这里，说明账号密码正确
        //需要继续向下验证其他三项信息

        //验证失效时间
        String expireTime = user.getExpireTime();//获取失效时间
        String currenTime = DateTimeUtil.getSysTime();//获取当前系统时间
        if (expireTime.compareTo(currenTime)<0){//说明已经过了有效时间
            throw new LoginException("账号失效");
        }

        //验证锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){//当前账号被锁定
            throw new LoginException("账号被锁定");
        }

        //验证ip的合法性
        String allip = user.getAllowIps();
        if (!allip.contains(ip)){
            throw new LoginException("ip地址不合法");
        }
        return user;
    }

    public List<User> getUserList(){
        List<User> list = userDao.getUserList();

        return list;
    }


}







