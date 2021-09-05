package com.zijing.crm.settings.dao;

import com.zijing.crm.settings.domin.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User login(Map<String, String> map);
    List<User> getUserList();

}
