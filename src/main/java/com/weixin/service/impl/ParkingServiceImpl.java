package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.dao.ParkingMapper;
import com.weixin.pojo.Result;
import com.weixin.service.ParkingService;
import com.weixin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/3/2.
 */
@Service
public class ParkingServiceImpl implements ParkingService{

    @Autowired
    private ParkingMapper parkingMapper;

    private final static Logger logger = LoggerFactory.getLogger(ParkingServiceImpl.class);


    /* #################################################  start小程序和网页端地图（停车场）接口  #################################################*/

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
        for(int i = 0;i<arrList.size();i++){
            if(arrList.get(i).length==5){
                System.out.println(arrList.get(i)[0]+","+arrList.get(i)[1]+","+arrList.get(i)[2]+","+arrList.get(i)[3]+","+arrList.get(i)[4]);
            }
        }
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for(int i = 0;i<arrList.size();i++){
            Map<String,Object> temp = new HashMap<String,Object>();
            if(arrList.get(i).length==4){
                System.out.println(arrList.get(i));
            }
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


    /* #################################################  end小程序和网页端地图（停车场）接口  #################################################*/




    /* #################################################  start小程序接口  #################################################*/

    /**
     * 获取车辆列表
     * @param token
     * @return
     */
    public Result getCarList(String token) throws Exception{
        List<Map<String,Object>> carList = parkingMapper.getCarList(token);
        for(int i = 0;i<carList.size();i++){
            Map<String,Object> temp = carList.get(i);
            if(temp.get("card_bank")!=null&&!"".equals(temp.get("card_bank"))){
                String str = temp.get("card_bank")+"("+temp.get("card_num_last")+")";
                temp.put("isbind","已绑卡");
                temp.put("card_num_last",str);
            }else{
                temp.put("isbind","未绑卡");
                temp.put("card_bank","");
            }
        }
        String str = JSON.toJSON(carList).toString();
        Result result = ResultUtil.requestSuccess(str);
        return result;
    }

    /**
     * 我的行程
     * @param param
     *      token
     *      platenum 车牌号 为空查询所有
     * @return
     */
    public Result getRouteList(Map<String,Object> param) throws Exception{
        List<Map<String,Object>> carList = parkingMapper.getRouteList(param);
        DecimalFormat df = new DecimalFormat("0");
        for(int i = 0;i<carList.size();i++){
            Map<String,Object> temp = carList.get(i);
            if(temp.get("couponamount")==null||"".equals(temp.get("couponamount"))){
                temp.put("iscoupon","0");//没有优惠
            }else{
                temp.put("iscoupon","1");//优惠了
            }
            int duration = Integer.parseInt(temp.get("duration").toString());
            if(duration<60){
                temp.put("duration",df.format(duration%60)+"分钟");
            }else{
                temp.put("duration",df.format(duration/60)+"小时"+df.format(duration%60)+"分钟");
            }
        }
        return ResultUtil.requestSuccess(JSONObject.toJSONString(carList));
    }


    /**
     * 添加绑定车辆
     * @param param
     *      token
     *      platenum 车牌号
     * @return
     */
    public Result insertBaseUserCar(Map<String,Object> param) throws Exception{
        String username = parkingMapper.getWechatUserByToken(param.get("token").toString());  //根据token查询用户手机号
        int count = parkingMapper.getPlateNumCountByUserName(username);   //查询 某个手机号下是否有车牌存在
        int isactive = 100;  //如果是第一次绑定车辆 isactive  初始化为100
        String is_default = "1";  //是默认车辆
        if(count>0){
            is_default = "0";  //不是默认车辆
            int maxIsactive = parkingMapper.getMaxIsactiveByUserName(username);
            logger.info("添加绑定车辆-->"+username+"用户不是第一次绑定车辆");
            maxIsactive = maxIsactive++;
        }
        param.put("username",username);
        param.put("isactive",isactive);
        param.put("is_default",is_default);
        logger.info("添加绑定车辆-->准备添加时参数:["+param+"]");
        int addCount = parkingMapper.insertBaseUserCar(param);
        if(addCount>0){
            logger.info("添加绑定车辆-->车辆绑定成功");
            return ResultUtil.requestSuccess("车辆绑定成功","车辆绑定成功");
        }else{
            logger.info("添加绑定车辆-->车辆绑定受影响行数0行");
            return ResultUtil.requestSuccess("车辆绑定受影响行数0行","车辆绑定受影响行数0行","01");
        }
    }


    /**
     * 删除绑定车辆信息
     * @param param
     *      token
     *      platenum 车牌号
     * @return
     * @throws Exception
     */
    public Result updateBaseUserCar(Map<String,Object> param) throws Exception{
        String username = parkingMapper.getWechatUserByToken(param.get("token").toString());  //根据token查询用户手机号
        param.put("username",username);
        param.put("status","01");
        logger.info("删除绑定车辆信息-->准备删除时参数:["+param+"]");
        int count  = parkingMapper.updateBaseUserCar(param);
        if(count>0){
            logger.info("删除绑定车辆信息-->成功解绑["+param.get("platenum")+"]车牌号");
            return ResultUtil.requestSuccess("成功解绑["+param.get("platenum")+"]车牌号","成功解绑["+param.get("platenum")+"]车牌号");
        }else{
            logger.info("删除绑定车辆信息-->["+param.get("platenum")+"],此车牌号解绑时受影响行数0行");
            return ResultUtil.requestSuccess("["+param.get("platenum")+"],此车牌号解绑时受影响行数0行","["+param.get("platenum")+"],此车牌号解绑时受影响行数0行","01");
        }
    }


    /**
     * 设置默认车辆
     * @param param
     *      token
     *      platenum 车牌号
     * @return
     * @throws Exception
     */
    public Result settingDefaultCar(Map<String,Object> param) throws Exception{
        String username = parkingMapper.getWechatUserByToken(param.get("token").toString());  //根据token查询用户手机号
        int min = parkingMapper.getMinIsactiveByUserName(username); //获取某个手机号下最早绑定的车牌号顺序
        min = min--;
        String platenum = param.get("platenum").toString();
        param.put("platenum",null);
        param.put("username",username);
        param.put("is_default","0");
        logger.info("设置默认车辆-->先把该手机号下的所有车辆都修改为非默认-->准备修改时参数:["+param+"]");
        int count = parkingMapper.updateBaseUserCar(param); //先把该手机号下的所有车辆都修改为非默认
        if(count>0){
            param.put("platenum",platenum);
            param.put("is_default","1");
            int count1 = parkingMapper.updateBaseUserCar(param); //设置某个车牌号为默认车辆
            if(count1>0){
                return ResultUtil.requestSuccess("["+platenum+"]车牌号,成功设置为默认车辆","["+platenum+"]车牌号,成功设置为默认车辆");
            }else{
                logger.info("设置默认车辆--> 设置某个车牌号为默认车辆-->受影响行数0行");
                return ResultUtil.requestSuccess("设置默认车辆时受影响行数0行","设置默认车辆时受影响行数0行","01");
            }
        }else{
            logger.info("设置默认车辆--> 先把该手机号下的所有车辆都修改为非默认-->受影响行数0行");
            return ResultUtil.requestSuccess("设置默认车辆时受影响行数0行","设置默认车辆时受影响行数0行","01");
        }
    }


    /**
     * 代扣开关
     * @param param
     *      token
     *      platenum 车牌号
     *      is_open_unionpay 银联代扣开启状态(0为关闭，1为开启)
     * @return
     * @throws Exception
     */
    public Result withholdSwitch(Map<String,Object> param) throws Exception{
        int isbindcard = parkingMapper.getPlateNumIsBindCard(param.get("platenum").toString()); //获取 某个车牌号 是否绑卡
        if(isbindcard>0){
            String username = parkingMapper.getWechatUserByToken(param.get("token").toString());  //根据token查询用户手机号
            param.put("username",username);
            logger.info("代扣开关-->准备修改时参数:["+param+"]");
            int count = parkingMapper.updateBaseUserCar(param);
            String is_open_name = null;
            if(param.get("is_open_unionpay").equals("0")){ //关闭
                is_open_name = "关闭";
            }else{
                is_open_name = "开启";
                param.put("limit_amount",200);
            }
            if(count>0){
                return ResultUtil.requestSuccess("["+param.get("platenum")+"]车牌号,"+is_open_name+"代扣成功","["+param.get("platenum")+"]车牌号,"+is_open_name+"代扣成功");
            }else{
                return ResultUtil.requestSuccess("["+param.get("platenum")+"]车牌号,"+is_open_name+"代扣时受影响行数0行","["+param.get("platenum")+"]车牌号,"+is_open_name+"代扣时受影响行数0行","01");
            }
        }
        return ResultUtil.requestSuccess("["+param.get("platenum")+"]车牌号,未绑卡","["+param.get("platenum")+"]车牌号,未绑卡","01");
    }

    /**
     * 根据token 获取手机号 咻币  手机号下绑定的车牌号列表
     * @param param
     *      {"token":""}
     * @return
     */
    public Result getUserNameAndYiXiMoneyByToken(Map<String,Object> param) throws Exception{
        logger.info("根据token 获取手机号 咻币 service");
        Map<String,Object> returnMap = parkingMapper.getUserNameAndYiXiMoneyByToken(param.get("token").toString());
        logger.info("根据token 获取手机号 咻币  返回数据:"+returnMap);
        List<String> plateNumList = parkingMapper.getPlateNumList(param.get("token").toString());
        logger.info("根据token 获取手机号下绑定的车牌号列表  返回数据:"+plateNumList);
        returnMap.put("plateNumList",plateNumList);
        return ResultUtil.requestSuccess(JSON.toJSON(returnMap).toString());
    }



    /* #################################################  end小程序接口  #################################################*/


}
