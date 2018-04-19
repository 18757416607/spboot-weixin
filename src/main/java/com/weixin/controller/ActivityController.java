package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.service.ActivityService;
import com.weixin.service.CouponService;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 * 活动  controller
 */
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    /**
     * 用户活跃数递增
     * @return
     */
    @PostMapping(value = "/activityActiveNum", produces = {"application/json;charset=UTF-8;"})
    public Result activityActiveNum(@RequestBody String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            /*if(paramMap.get("activityId")==null||"".equals(paramMap.get("activityId"))){
                return ResultUtil.requestFaild("[activityId]参数为空");
            }
            if(paramMap.get("activityName")==null||"".equals(paramMap.get("activityName"))){
                return ResultUtil.requestFaild("[activityName]参数为空");
            }*/
            /*if(paramMap.get("token")==null||"".equals(paramMap.get("token"))){
                return ResultUtil.requestFaild("[token]参数为空");
            }*/
            //int count = activityService.activityActiveNum(paramMap.get("activityId").toString(),paramMap.get("activityName").toString(),paramMap.get("openid").toString());
           //return activityService.activityActiveNum("2018-CY","2018春游送券活动",paramMap.get("token").toString());
            return ResultUtil.requestSuccess("踩踩踩踩踩踩");
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }



}
