package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.service.CouponService;
import com.weixin.util.DateUtils;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(CouponController.class);

    @Autowired
    private CouponService couponService;

    /* #################################################  start春游活动  #################################################*/

    /**
     * 2018年 春游活动 随机分配一张优惠券  1元 2元 3元 5元
     * @param paramStr
     * @return
     */
    @PostMapping(value = "/allocationOneCoupon"/*, produces = {"application/json;charset=UTF-8;"}*/)
    public Result randomAllocationOneCoupon(/*@RequestBody*/ String paramStr){
        try{
            logger.info("进入2018年 春游活动领券controller,参数:["+paramStr+"]");
            String endDate = "2018-04-18 23:59:59"; //活动到期时间
            int judge = DateUtils.compare_date(new Date().getTime(),DateUtils.formatStrToDate1("2018-04-18 23:59:59").getTime());
            if(judge==1){
                logger.info("春游领券活动已到期,请关注下个活动!");
                return ResultUtil.requestSuccess(null,"春游领券活动已到期,请关注下个活动","03");
            }
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));

            //需要判断是否在活动期限内

            /*Map<String,Object> param = new HashMap<String,Object>();
            param.put("username",paramMap.get("username"));*/
            return couponService.randomAllocationOneCoupon(paramMap.get("token").toString());
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /* #################################################  end春游活动  #################################################*/






    /* #################################################  start小程序接口  #################################################*/

    /**
     * 获取优惠券列表
     * @param paramStr
     *      token
     * @return
     */
    @PostMapping(value = "/getCouponList")
    public Result getCouponList(String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            return couponService.getCouponList(paramMap.get("token").toString());
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }















    /* #################################################  end小程序接口  #################################################*/

}
