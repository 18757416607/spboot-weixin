package com.weixin.smallRoutinePay.service;

import com.weixin.pojo.Result;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/12.
 */
public interface AppPayService {

    /**
     * 停车费用
     * @param param
     * @return
     */
    public Result parkStopCost(Map param) throws  Exception;


}
