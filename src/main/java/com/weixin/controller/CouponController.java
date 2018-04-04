package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.service.CouponService;
import com.weixin.util.DateUtils;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @PostMapping(value = "/allocationOneCoupon"/*, produces = {"application/json;charset=UTF-8;"}*/)
    public Result randomAllocationOneCoupon(/*@RequestBody*/ String paramStr){
        try{
            System.out.println("进入领券url");
            String endDate = "2018-04-12 09:11:34"; //活动到期时间
            int judge = DateUtils.compare_date(new Date().getTime(),DateUtils.formatStrToDate1("2018-04-11 23:59:59").getTime());
            if(judge==1){
                return ResultUtil.requestSuccess(null,"春游领券活动已到期,请关注下个活动","03");
            }
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));

            //需要判断是否在活动期限内

            /*Map<String,Object> param = new HashMap<String,Object>();
            param.put("username",paramMap.get("username"));*/
            return couponService.randomAllocationOneCoupon(paramMap.get("token").toString());
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


}
