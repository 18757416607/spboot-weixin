package com.weixin.service;

import com.weixin.pojo.Result;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface ParkingService {

    public int checkDataIsValid(Map<String,String> param);

    public Result addBatchNonCooperationPark() throws Exception;

    public Result findNearbyPark(Map<String,String> param) throws  Exception;

}
