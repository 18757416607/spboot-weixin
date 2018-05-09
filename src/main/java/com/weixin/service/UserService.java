package com.weixin.service;

import com.weixin.pojo.Result;
import com.weixin.pojo.User;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/7.
 */
public interface UserService {

    /**
     * 获取  我的优惠券个数
     * @param param
     *      {"token":""}
     * @return
     */
    public Result getMyCouponCount(Map<String,Object> param) throws Exception;

    /**
     * 获取  我的优惠券列表
     * @param param
     *      {"token":""}
     * @return
     */
    public Result getMyCouponList(Map<String,Object> param) throws Exception;


}
