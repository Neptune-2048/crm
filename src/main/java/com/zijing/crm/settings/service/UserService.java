package com.zijing.crm.settings.service;

import com.zijing.crm.exception.LoginException;
import com.zijing.crm.settings.domin.User;
import java.util.List;


public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
    List<User> getUserList();
}
