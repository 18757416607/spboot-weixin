package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.pojo.User;
import com.weixin.service.ParkingService;
import com.weixin.service.UserService;
import com.weixin.util.JsonUtil;
import com.weixin.util.MathUtils;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/7.
 */
@ControllerAdvice
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 获取 我的优惠券个数
     * @param paramStr
     *      {"token":""}
     * @return
     */
    @PostMapping(value = "/getMyCouponCount")
    public Result getMyCouponCount(String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("进入获取 我的优惠券个数controller-->参数：["+paramMap+"]");
            return userService.getMyCouponCount(paramMap);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


    /**
     * 获取 我的优惠券列表
     * @param paramStr
     *      {"token":""}
     * @return
     */
    @PostMapping(value = "/getMyCouponList")
    public Result getMyCouponList(String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("进入获取 我的优惠券列表controller-->参数：["+paramMap+"]");
            return userService.getMyCouponList(paramMap);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


}
