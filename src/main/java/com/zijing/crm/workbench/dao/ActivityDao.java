package com.zijing.crm.workbench.dao;

import com.zijing.crm.workbench.domin.Activity;
import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getActivityByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Activity getByid(String id);

    int update(Activity activity);

    Activity detail(String id);
}
