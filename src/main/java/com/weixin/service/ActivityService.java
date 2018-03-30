package com.weixin.service;

/**
 * Created by Administrator on 2018/3/29.
 */
public interface ActivityService {

    /**
     * 用户活跃数递增
     * @param activityId
     * @param activityName
     * @return
     */
    public int activityActiveNum(String activityId,String activityName,String openid);


}
