package com.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.weixin.pojo.Result;
import com.weixin.service.ParkingService;
import com.weixin.thread.TokenThread;
import com.weixin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */
@RestController
@RequestMapping(value = "/parking")
public class ParkingController {

    private final static Logger logger = LoggerFactory.getLogger(ParkingController.class);

    @Resource
    private ParkingService parkingService;

    /**
     * 批量添加停车场 txt
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
     * 批量添加停车场 excel
     * @return   oMWvv0Lbz5YcD2y_EUi_iqriaK1A
     */
    @PostMapping("/addBatchNonCooperationPark_excel")
    public Result addBatchNonCooperationPark_excel(@RequestParam(value = "excelFile") MultipartFile excelFile, HttpServletRequest req, HttpServletResponse resp){;
        try {
            return parkingService.addBatchNonCooperationPark_excel(excelFile);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


    /**
     * 网页端
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
            logger.info("controller-->网页端-->利用经纬度查询附近停车场-->"+paramMap);
            return parkingService.findNearbyPark(param);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /**
     * 小程序端
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场
     * @return
     */
    @PostMapping(value = "/findNearbyParkNew",produces = {"application/json;charset=UTF-8;"})
    public Result findNearbyParkNew(@RequestBody String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            String longitude = paramMap.get("longitude").toString();  //搜索经度
            String latitude = paramMap.get("latitude").toString();    //搜索纬度
            String mylongitude = paramMap.get("mylongitude").toString();  //用户当前位置经度
            String mylatitude = paramMap.get("mylatitude").toString();    //用户当前位置纬度
            Map<String,String> param = new HashMap<String,String>();
            param.put("longitude",longitude);
            param.put("latitude",latitude);
            param.put("mylongitude",mylongitude);
            param.put("mylatitude",mylatitude);
            logger.info("controller-->小程序端-->利用经纬度查询附近停车场-->"+paramMap);
            return parkingService.findNearbyParkNew(param);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /**
     * 获取所有的合作停车场列表
     * @return
     */
    @PostMapping(value = "/findParkList" ,produces = {"application/json;charset=UTF-8;"})
    public Result findParkList(){
        logger.info("controller-->获取所有的合作停车场列表");
        return parkingService.findPrakList();
    }


}
