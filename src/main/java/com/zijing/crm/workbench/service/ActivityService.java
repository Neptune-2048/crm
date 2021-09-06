package com.zijing.crm.workbench.service;

import com.zijing.crm.vo.PaginationVo;
import com.zijing.crm.workbench.domin.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getByid(String id);

    boolean update(Activity activity);
}
