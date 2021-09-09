package com.zijing.crm.workbench.service;

import com.zijing.crm.vo.PaginationVo;
import com.zijing.crm.workbench.domin.Activity;
import com.zijing.crm.workbench.domin.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getByid(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String id);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    Map<String,Object> updateRemark(ActivityRemark activityRemark);
}
