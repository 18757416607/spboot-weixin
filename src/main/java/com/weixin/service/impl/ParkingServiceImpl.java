package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixin.dao.ParkingMapper;
import com.weixin.pojo.Result;
import com.weixin.service.ParkingService;
import com.weixin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
     * 批量添加未合作停车场信息
     * @return
     */
    public Result addBatchNonCooperationPark_excel(MultipartFile excelFile) throws Exception{
        List<String[]> arrList = PoiUtils.readExcel(excelFile);
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for(int i = 0;i<arrList.size();i++){
            Map<String,Object> temp = new HashMap<String,Object>();
            temp.put("name",arrList.get(i)[5]);
            temp.put("address",arrList.get(i)[0]);
            temp.put("address_loc","POINT("+arrList.get(i)[3]+" "+arrList.get(i)[2]+")");
            mapList.add(temp);
        }
        Integer count = parkingMapper.addBatchNonCooperationPark(mapList);
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
        List<Map<String,Object>> list = parkingMapper.findNearbyPark(param);
        Map<String,Object> map = new HashMap<String,Object>();;
        List<Map<String,Object>> cooperationList = new ArrayList<Map<String,Object>>();  //存放合作的停车场信息
        List<Map<String,Object>> non_cooperationList = new ArrayList<Map<String,Object>>(); //存放未合作的停车场信息
        if(list.size()>0){
            for(int i = 0;i<list.size();i++){
                Map<String,Object> temp = list.get(i);
                if(temp.get("latitude")!=null&&!"".equals(temp.get("latitude"))){
                    if(temp.get("longitude")!=null&&!"".equals(temp.get("longitude"))){
                        int distance = (int)MapUtil.GetDistance(Double.parseDouble(param.get("latitude")),Double.parseDouble(param.get("longitude")),Double.parseDouble(temp.get("latitude").toString()),Double.parseDouble(temp.get("longitude").toString()));
                        temp.put("distance",Integer.toString(distance));
                    }
                }
                if(temp.get("mark").equals("00")){
                    temp.put("carstatus","00");
                    temp.put("carstatusname","未知");
                    temp.put("color","666666");   //未知
                    non_cooperationList.add(temp);
                }else if(temp.get("mark").equals("11")){
                    if(temp.get("carnum")!=null&&!"".equals(temp.get("carnum"))){
                        if(Integer.parseInt(temp.get("carnum").toString())>20){
                            temp.put("carstatus","01"); //空闲
                            temp.put("carstatusname","空闲");
                            temp.put("color","4cae4c");
                        }else{
                            temp.put("carstatus","02");  //紧张
                            temp.put("carstatusname","紧张");
                            temp.put("color","CC0000");
                        }
                    }else{
                        temp.put("carstatus","00");  //未知
                        temp.put("carstatusname","未知");
                        temp.put("color","666666");
                    }
                    cooperationList.add(temp);
                }
                String name = temp.get("name").toString();
                if(name.contains("停车场(")){
                    temp.put("name",name.substring(name.indexOf("(")+1,name.lastIndexOf(")")));
                }
            }
            map.put("cooperationList",cooperationList);
            map.put("non_cooperationList",non_cooperationList);
            return ResultUtil.requestSuccess(JSON.toJSON(map).toString());
        }else{
            map.put("cooperationList",cooperationList);
            map.put("non_cooperationList",non_cooperationList);
            return ResultUtil.requestSuccess(JSON.toJSON(map).toString());
        }
    }


    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * map.cooperationList  存放合作的停车场信息
     * map.non_cooperationList  存放未合作的停车场信息
     * @param param
     * @return
     */
    public Result findNearbyParkNew(Map<String,String> param) throws  Exception{
        List<Map<String,Object>> list = parkingMapper.findNearbyPark(param);
        Map<String,Object> map = new HashMap<String,Object>();;
        List<Map<String,Object>> cooperationList = new ArrayList<Map<String,Object>>();  //存放合作的停车场信息
        List<Map<String,Object>> non_cooperationList = new ArrayList<Map<String,Object>>(); //存放未合作的停车场信息
        if(list.size()>0){
            for(int i = 0;i<list.size();i++){
                Map<String,Object> temp = list.get(i);
                if(temp.get("latitude")!=null&&!"".equals(temp.get("latitude"))){
                    if(temp.get("longitude")!=null&&!"".equals(temp.get("longitude"))){
                        int distance = (int)MapUtil.GetDistance(Double.parseDouble(param.get("mylatitude")),Double.parseDouble(param.get("mylongitude")),Double.parseDouble(temp.get("latitude").toString()),Double.parseDouble(temp.get("longitude").toString()));
                        temp.put("distance",Integer.toString(distance));
                    }
                }
                if(temp.get("mark").equals("00")){
                    temp.put("carstatus","00");
                    temp.put("carstatusname","未知");
                    temp.put("color","666666");   //未知
                    non_cooperationList.add(temp);
                }else if(temp.get("mark").equals("11")){
                    if(temp.get("carnum")!=null&&!"".equals(temp.get("carnum"))){
                        if(Integer.parseInt(temp.get("carnum").toString())>20){
                            temp.put("carstatus","01"); //空闲
                            temp.put("carstatusname","空闲");
                            temp.put("color","4cae4c");
                        }else{
                            temp.put("carstatus","02");  //紧张
                            temp.put("carstatusname","紧张");
                            temp.put("color","CC0000");
                        }
                    }else{
                        temp.put("carstatus","00");  //未知
                        temp.put("carstatusname","未知");
                        temp.put("color","666666");
                    }
                    cooperationList.add(temp);
                }
                String name = temp.get("name").toString();
                if(name.contains("停车场(")){
                    temp.put("name",name.substring(name.indexOf("(")+1,name.lastIndexOf(")")));
                }
            }
            System.out.println("cooperationList size:"+cooperationList.size());

            double latitude = Double.parseDouble(param.get("latitude").toString());   //搜索纬度
            double longitude = Double.parseDouble(param.get("longitude").toString()); //搜索经度
            for(int i = 0;i<cooperationList.size();i++){
                //Map<String,Object> temp = new HashMap<String,Object>();
                Map<String,Object> temp = cooperationList.get(i);
                double s_latitude = Double.parseDouble(cooperationList.get(i).get("latitude").toString());
                double s_longitude = Double.parseDouble(cooperationList.get(i).get("longitude").toString());
                double distance = LocationUtils.getDistance(latitude,longitude,s_latitude,s_longitude); //计算出搜索距离和每个停车场间的距离
                temp.put("distance",distance);
            }

            for(int i=0;i<cooperationList.size();i++){
                for(int j=0;j<cooperationList.size()-1;j++){
                    double distance = Double.parseDouble(cooperationList.get(i).get("distance").toString());
                    double distance2 = Double.parseDouble(cooperationList.get(j).get("distance").toString());
                    if(distance>distance2){
                        Map<String,Object>  temp = cooperationList.get(i);
                        cooperationList.set(i, cooperationList.get(j));
                        cooperationList.set(j, temp);
                    }
                }
            }

            List<Map<String,Object>> cooperationList2 = new ArrayList<Map<String,Object>>();
            for(int i = cooperationList.size()-1;i>0;i--){
                cooperationList2.add(cooperationList.get(i));
            }

            map.put("cooperationList",cooperationList2);
            map.put("non_cooperationList",non_cooperationList);
            return ResultUtil.requestSuccess(JSON.toJSON(map).toString());
        }else{
            map.put("cooperationList",cooperationList);
            map.put("non_cooperationList",non_cooperationList);
            return ResultUtil.requestSuccess(JSON.toJSON(map).toString());
        }
    }


        public static void main(String[] args) {
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();

            list.add(getData(0));
            list.add(getData(3));
            list.add(getData(05));
            list.add(getData(6));
            list.add(getData(2));

            System.out.println("排序前" + list);

            Collections.sort(list, new Comparator<Map<String, String>>() {
                public int compare(Map<String, String> o1, Map<String, String> o2) {
                    return o1.get("distance").compareTo(o2.get("distance"));
                }
            });

            System.out.println("排序后" + list);
        }

        private static Map<String, String> getData(int num) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("countScore", String.valueOf(num));
            return map;
        }


    /**
     * 获取所有的合作停车场列表
     * @return
     */
    public Result findPrakList(){
        try{
            List<Map<String,Object>> list = parkingMapper.findPrakList();
            for(int i = 0;i<list.size();i++){
                Map<String,Object> temp = list.get(i);
                if(temp.get("carnum")!=null&&!"".equals(temp.get("carnum"))){
                    if(Integer.parseInt(temp.get("carnum").toString())>20){
                        temp.put("carstatus","01"); //空闲
                        temp.put("carstatusname","空闲");
                        temp.put("color","4cae4c");
                    }else{
                        temp.put("carstatus","02");  //紧张
                        temp.put("carstatusname","紧张");
                        temp.put("color","CC0000");
                    }
                }else{
                    temp.put("carstatus","00");  //未知
                    temp.put("carstatusname","未知");
                    temp.put("color","666666");
                }
            }
            String result = JSON.toJSON(list).toString();
            return ResultUtil.requestSuccess(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(ResultUtil.REQUESTFAILD);
        }

    }


}
