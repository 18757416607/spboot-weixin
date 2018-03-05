package com.wexin.controller;

import com.wexin.pojo.Result;
import com.wexin.service.ParkingService;
import com.wexin.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
        int count = parkingService.addBatchNonCooperationPark();
        if(count>0){
            return ResultUtil.requestSuccess(null);
        }
        return ResultUtil.requestFaild(null);
    }

    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     */
    @GetMapping(value = "/findNearbyPark")
    public Result findNearbyPark(String longitude,String latitude){
        Map<String,String> param = new HashMap<String,String>();
        param.put("longitude",longitude);
        param.put("latitude",latitude);
        return ResultUtil.requestSuccess(parkingService.findNearbyPark(param));
    }

}
