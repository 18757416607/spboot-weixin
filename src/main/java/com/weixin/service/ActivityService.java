package com.weixin.service;

import com.weixin.pojo.Result;

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
    public Result activityActiveNum(String activityId, String activityName, String openid) throws Exception;


}
