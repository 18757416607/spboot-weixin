package com.weixin.service;

import com.weixin.pojo.Result;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */
public interface CouponService {

    /**
     * 随机分配一张优惠券
     * @return
     */
    public Result randomAllocationOneCoupon(String token)  throws Exception;



}
