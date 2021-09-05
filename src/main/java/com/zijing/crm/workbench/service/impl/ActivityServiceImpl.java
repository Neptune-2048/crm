package com.zijing.crm.workbench.service.impl;

import com.zijing.crm.utils.SqlSessionUtil;
import com.zijing.crm.vo.PaginationVo;
import com.zijing.crm.workbench.dao.ActivityDao;
import com.zijing.crm.workbench.dao.ActivityRemarkDao;
import com.zijing.crm.workbench.domin.Activity;

import com.zijing.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemark = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public boolean save(Activity activity) {
        //打标记
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        //取得total
        int total  = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> activity = activityDao.getActivityByCondition(map);

        //封装
        PaginationVo<Activity> vo = new PaginationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(activity);
        return vo;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = activityRemark.getCountByAids(ids);

        //删除备注，返回受影响的条数（实际删除的数量）
        int count2 = activityRemark.deleteByAids(ids);

        if (count1!=count2){
            flag = false;
        }

         //删除市场活动
        int count3 = activityDao.delete(ids);
        return flag;
    }


}
