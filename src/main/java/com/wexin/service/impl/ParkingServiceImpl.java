package com.wexin.service.impl;

import com.wexin.dao.ParkingMapper;
import com.wexin.service.ParkingService;
import com.wexin.util.FileUtils;
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
    public int addBatchNonCooperationPark(){
        List<Map<String,Object>> paramList = FileUtils.readeGaoDeMapParkInfo("D:/json.txt");
        return parkingMapper.addBatchNonCooperationPark(paramList);
    }


    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * map.cooperationList  存放合作的停车场信息
     * map.non_cooperationList  存放未合作的停车场信息
     * @param param
     * @return
     */
    public Map<String,Object> findNearbyPark(Map<String,String> param){
        List<Map<String,String>> list = parkingMapper.findNearbyPark(param);
        Map<String,Object> map = null;
        List<Map<String,String>> cooperationList = null;  //存放合作的停车场信息
        List<Map<String,String>> non_cooperationList = null; //存放未合作的停车场信息
        if(list.size()>0){
            map = new HashMap<String,Object>();
            cooperationList = new ArrayList<Map<String,String>>();
            non_cooperationList = new ArrayList<Map<String,String>>();
            for(int i = 0;i<list.size();i++){
                Map<String,String> temp = list.get(i);
                if(temp.get("mark").equals("00")){
                    non_cooperationList.add(temp);
                }else if(temp.get("mark").equals("11")){
                    cooperationList.add(temp);
                }
            }
            map.put("cooperationList",cooperationList);
            map.put("non_cooperationList",non_cooperationList);
        }
        return map;
    }

}
