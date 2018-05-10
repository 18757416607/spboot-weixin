package com.weixin.service;

import com.weixin.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface ParkingService {

    /* #################################################  start小程序和网页端地图（停车场）接口  #################################################*/

    /**
     * 验证数据是否有效
     * @param param
     * @return
     */
    public int checkDataIsValid(Map<String,String> param);

    /**
     * 批量添加未合作停车场信息
     * @return
     * @throws Exception
     */
    public Result addBatchNonCooperationPark() throws Exception;

    /**
     * 批量添加未合作停车场信息
     * @return
     * @throws Exception
     */
    public Result addBatchNonCooperationPark_excel(MultipartFile excelFile) throws Exception;


    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * @param param
     * @return
     * @throws Exception
     */
    public Result findNearbyPark(Map<String,String> param) throws  Exception;

    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * @param param
     * @return
     * @throws Exception
     */
    public Result findNearbyParkNew(Map<String,String> param) throws  Exception;

    /**
     * 获取所有的合作停车场列表
     * @return
     */
    public Result findPrakList();

    /* #################################################  end小程序和网页端地图（停车场）接口  #################################################*/





    /* #################################################  start小程序接口  #################################################*/

    /**
     * 获取车辆列表
     * @param token
     * @return
     */
    public Result getCarList(String token) throws Exception;

    /**
     * 我的行程
     * @param param
     *      token
     *      platenum 车牌号 为空查询所有
     * @return
     */
    public Result getRouteList(Map<String,Object> param) throws Exception;

    /**
     * 添加绑定车辆
     * @param param
     *      token
     *      platenum 车牌号
     * @return
     */
    public Result insertBaseUserCar(Map<String,Object> param)throws Exception;

    /**
     * 删除绑定车辆信息
     * @param param
     *      token
     *      platenum 车牌号
     * @return
     * @throws Exception
     */
    public Result updateBaseUserCar(Map<String,Object> param, HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 设置默认车辆
     * @param param
     *      token
     *      platenum 车牌号
     * @return
     * @throws Exception
     */
    public Result settingDefaultCar(Map<String,Object> param) throws Exception;

    /**
     * 代扣开关
     * @param param
     *      token
     *      platenum 车牌号
     *      is_open_unionpay 银联代扣开启状态(0为关闭，1为开启)
     * @return
     * @throws Exception
     */
    public Result withholdSwitch(Map<String,Object> param) throws Exception;


    /**
     * 根据token 获取手机号 咻币  手机号下绑定的车牌号列表
     * @param param
     *      {"token":""}
     * @return
     */
    public Result getUserNameAndYiXiMoneyByToken(Map<String,Object> param) throws Exception;




    /* #################################################  end小程序接口  #################################################*/

}
