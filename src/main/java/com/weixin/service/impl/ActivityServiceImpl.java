package com.weixin.service.impl;

import com.weixin.dao.ActivityMapper;
import com.weixin.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/3/29.
 */
@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 用户活跃数递增
     * @param activityId
     * @param activityName
     * @return
     */
    public int activityActiveNum(String activityId,String activityName,String openid){
        return activityMapper.insertActivity(activityId,activityName,openid);
    }

}
