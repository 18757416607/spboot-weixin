package com.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.weixin.dao.ActivityMapper;
import com.weixin.pojo.Result;
import com.weixin.service.ActivityService;
import com.weixin.util.Constant;
import com.weixin.util.RequestWx;
import com.weixin.util.ResultUtil;
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
    public Result activityActiveNum(String activityId, String activityName, String token) throws Exception{
        String unionid = activityMapper.getUnionidByToken(token);
         if(unionid==null||"".equals(unionid)){
             return ResultUtil.requestSuccess(null,"没有绑定手机,请先绑定手机号","01");
         }else{
             activityMapper.insertActivity(activityId,activityName,unionid);
             return ResultUtil.requestSuccess(null);
         }

    }

}
