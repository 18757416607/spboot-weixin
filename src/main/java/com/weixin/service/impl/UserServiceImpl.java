package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixin.dao.UserMapper;
import com.weixin.pojo.Result;
import com.weixin.pojo.User;
import com.weixin.service.UserService;
import com.weixin.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/7.
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;


    /**
     * 获取  我的优惠券个数
     * @param param
     *      {"token":""}
     * @return
     */
    public Result getMyCouponCount(Map<String,Object> param) throws Exception{
        logger.info("获取  我的优惠券个数 service");
        int couponCount = userMapper.getMyCouponCount(param.get("token").toString());
        logger.info("获取  我的优惠券个数 --> token:["+param.get("token")+"]下的优惠券个数:"+couponCount);
        return ResultUtil.requestSuccess(couponCount+"");
    }

    /**
     * 获取  我的优惠券列表
     * @param param
     *      {"token":""}
     * @return
     */
    public Result getMyCouponList(Map<String,Object> param) throws Exception{
        logger.info("获取  我的优惠券列表 service");
        List<Map<String,Object>> couponList = userMapper.getMyCouponList(param.get("token").toString());
        logger.info("获取  我的优惠券列表 --> token:["+param.get("token")+"]下的优惠券列表:"+couponList);
        return ResultUtil.requestSuccess(JSON.toJSON(couponList).toString());
    }


}
