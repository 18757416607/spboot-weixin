package com.weixin.service;

import com.weixin.pojo.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */
public interface CouponService {

    /* #################################################  start春游活动  #################################################*/
    /**
     * 随机分配一张优惠券
     * @return
     */
    public Result randomAllocationOneCoupon(String token)  throws Exception;

    /* #################################################  end春游活动  #################################################*/






    /* #################################################  start小程序接口  #################################################*/

    /**
     * 获取优惠券列表
     * @param token
     *      token
     * @return
     */
    public Result getCouponList(String token) throws Exception;
















    /* #################################################  end小程序接口  #################################################*/


}
