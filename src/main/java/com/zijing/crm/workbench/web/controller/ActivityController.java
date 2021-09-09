package com.zijing.crm.workbench.web.controller;



import com.zijing.crm.settings.domin.User;
import com.zijing.crm.settings.service.UserService;
import com.zijing.crm.settings.service.impl.UserServiceImpl;
import com.zijing.crm.utils.*;
import com.zijing.crm.vo.PaginationVo;
import com.zijing.crm.workbench.domin.Activity;
import com.zijing.crm.workbench.domin.ActivityRemark;
import com.zijing.crm.workbench.service.ActivityService;
import com.zijing.crm.workbench.service.impl.ActivityServiceImpl;
import org.apache.ibatis.annotations.Update;
import org.apache.taglibs.standard.tag.common.sql.DataSourceWrapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityController extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场控制器");
        String path = request.getServletPath();
        System.out.println(path);
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(request,response);
        }else if ("/workbench/activity/delete.do".equals(path)) {
            delete(request,response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/activity/showRemarkList.do".equals(path)){
            showRemarkList(request, response);
        }else if ("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request, response);
        }else if ("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request, response);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request, response);
        }
    }

    private void getUserList(HttpServletRequest request,HttpServletResponse response) {
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list = us.getUserList();
        PrintJson.printJsonObj(response,list);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
            //随机生产市场活动表中的id
            String  id = UUIDUtil.getUUID();
            String  Owner =  request.getParameter("create-Owner");//接收的是所有者的即用户的id，而不是用户的name
            String  name =  request.getParameter("create-name");//市场活动名称
            String  startDate =  request.getParameter("create-startDate");
            String  endDate =  request.getParameter("create-endDate");
            String  cost =  request.getParameter("create-cost");
            String  description =  request.getParameter("create-description");
            //创建时间，当前系统时间
            String createTime = DateTimeUtil.getSysTime();
            //创建人
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("user");
            String createBy = user.getName();//这里是创建该市场活动的人，不要和所有者给搞混了

            //数据封装在activity市场活动对象中
            Activity activity = new Activity();
            activity.setId(id);
            activity.setOwner(Owner);
            activity.setName(name);
            activity.setStartDate(startDate);
            activity.setEndDate(endDate);
            activity.setCost(cost);
            activity.setDescription(description);
            activity.setCreateTime(createTime);
            activity.setCreateBy(createBy);

            //获取代理对象，通过代理对象来实现业务的逻辑
            ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
            //调用代理对象实现业务的方法，并接收处理结果
            boolean flag = as.save(activity);
            //将处理结果以json格式来返回给前端
            PrintJson.printJsonFlag(response,flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String Owner = request.getParameter("Ower");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //页码
        String pageNoStr = request.getParameter("pageNo");
        System.out.println(pageNoStr+"===============================");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pagesizeStr = request.getParameter("pagesize");
        int pageSize = Integer.valueOf(pagesizeStr);
        //计算出省略过的记录数
        int skipCount = (pageNo-1) *pageSize;

        //存放在Map中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("name",name);
        map.put("Owenr",Owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVo<Activity> vo = as.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.delete(ids);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        System.out.println(id);
        //代理对象只能有一个，如果出现多个会出异常。
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = as.getByid(id);
        PrintJson.printJsonObj(response,map);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        //请求的参数
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name  = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description =request.getParameter("description");
        //获取修改时间，以及修改人
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();
        //封装数据
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        //获取代理对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.update(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = as.detail(id);
        request.setAttribute("activity",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);

    }

    private void showRemarkList(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> list = as.getRemarkListByAid(id);
        PrintJson.printJsonObj(response,list);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        String activityId = request.getParameter("activityId");
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setActivityId(activityId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.saveRemark(activityRemark);
        Map map = new HashMap();
        map.put("success",flag);
        map.put("activityRemark",activityRemark);
        PrintJson.printJsonObj(response,map);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditFlag("1");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = as.updateRemark(activityRemark);
        PrintJson.printJsonObj(response,map);
    }

}
