package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.service.CouponService;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 * 优惠卷
 */
@RestController
@RequestMapping(value = "/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 2018年 春游活动 随机分配一张优惠券  1元 2元 3元 5元
     * @param paramStr
     * @return
     */
    @PostMapping(value = "/randomAllocationOneCoupon", produces = {"application/json;charset=UTF-8;"})
    public Result randomAllocationOneCoupon(@RequestBody String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            Map<String,Object> param = new HashMap<String,Object>();
            param.put("username",paramMap.get("username"));
            return couponService.randomAllocationOneCoupon(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }




}
