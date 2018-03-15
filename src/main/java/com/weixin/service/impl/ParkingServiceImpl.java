package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixin.dao.ParkingMapper;
import com.weixin.pojo.Result;
import com.weixin.service.ParkingService;
import com.weixin.util.FileUtils;
import com.weixin.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
@Service
public class ParkingServiceImpl implements ParkingService{

    @Autowired
    private ParkingMapper parkingMapper;

    /**
     * 验证数据是否有效
     * @param param
     * @return
     */
    public int checkDataIsValid(Map<String,String> param){
        return parkingMapper.checkDataIsValid(param);
    }

    /**
     * 批量添加未合作停车场信息
     * @return
     */
    public Result addBatchNonCooperationPark() throws Exception{
        List<Map<String,Object>> paramList = FileUtils.readeGaoDeMapParkInfo("D:/外滩方圆1公里停车场.txt");
        for(int i = 0;i<paramList.size();i++){

        }
        Integer count = parkingMapper.addBatchNonCooperationPark(paramList);
        return ResultUtil.requestSuccess(count+"条停车场信息批量添加成功");
    }


    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * map.cooperationList  存放合作的停车场信息
     * map.non_cooperationList  存放未合作的停车场信息
     * @param param
     * @return
     */
    public Result findNearbyPark(Map<String,String> param) throws  Exception{
        List<Map<String,String>> list = parkingMapper.findNearbyPark(param);
        Map<String,Object> map = new HashMap<String,Object>();;
        List<Map<String,String>> cooperationList = new ArrayList<Map<String,String>>();  //存放合作的停车场信息
        List<Map<String,String>> non_cooperationList = new ArrayList<Map<String,String>>(); //存放未合作的停车场信息
        if(list.size()>0){
            for(int i = 0;i<list.size();i++){
                Map<String,String> temp = list.get(i);
                if(temp.get("mark").equals("00")){
                    non_cooperationList.add(temp);
                    temp.put("carstatus","未知");
                }else if(temp.get("mark").equals("11")){
                    cooperationList.add(temp);
                    if(temp.get("carnum")!=null&&!"".equals(temp.get("carnum"))){
                        if(Integer.parseInt(temp.get("carnum"))>20){
                            temp.put("carstatus","空闲");
                        }else{
                            temp.put("carstatus","紧张");
                        }
                    }else{
                        temp.put("carstatus","未知");
                    }
                }
                String name = temp.get("name").toString();
                if(name.contains("停车场(")){
                    temp.put("name",name.substring(name.indexOf("(")+1,name.lastIndexOf(")")));
                }
            }
            map.put("cooperationList",cooperationList);
            map.put("non_cooperationList",non_cooperationList);
            System.out.println("-------------------------------------");
            System.out.println(map);
            System.out.println("-------------------------------------");
            return ResultUtil.requestSuccess(JSON.toJSON(map).toString());
        }else{
            map.put("cooperationList",cooperationList);
            map.put("non_cooperationList",non_cooperationList);
            return ResultUtil.requestSuccess(JSON.toJSON(map).toString());
        }

    }


    /**
     * 获取所有的合作停车场列表
     * @return
     */
    public Result findPrakList(){
        try{
            String result = JSON.toJSON(parkingMapper.findPrakList()).toString();
            return ResultUtil.requestSuccess(result);
        }catch (Exception e){
            return ResultUtil.requestFaild(ResultUtil.REQUESTFAILD);
        }

    }


}
