package com.zijing.crm.workbench.dao;

import com.zijing.crm.workbench.domin.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String id);

    int deleteRemarkById(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
