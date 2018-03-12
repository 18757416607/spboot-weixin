package com.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.weixin.pojo.Result;
import com.weixin.service.ParkingService;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */
@RestController
@RequestMapping(value = "/parking")
public class ParkingController {

    @Resource
    private ParkingService parkingService;

    /**
     * 批量添加停车场
     * @return
     */
    @PostMapping(value = "/addBatchNonCooperationPark")
    public Result addBatchNonCooperationPark(){
        try{
            return parkingService.addBatchNonCooperationPark();
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场
     * @return
     */
    @PostMapping(value = "/findNearbyPark",produces = {"application/json;charset=UTF-8;"})
    public Result findNearbyPark(@RequestBody String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            String longitude = paramMap.get("longitude").toString();  //经度
            String latitude = paramMap.get("latitude").toString();    //纬度
            Map<String,String> param = new HashMap<String,String>();
            param.put("longitude",longitude);
            param.put("latitude",latitude);
            return parkingService.findNearbyPark(param);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    @PostMapping(value = "/ttt",produces = {"application/json;charset=UTF-8;"})
    public Result t()  throws Exception{
        return ResultUtil.requestSuccess("");
    }


}
