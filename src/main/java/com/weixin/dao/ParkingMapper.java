package com.weixin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
@Repository
@Mapper
public interface ParkingMapper{

    /* #################################################  start小程序和网页端地图（停车场）接口  #################################################*/

    /**
     * 验证数据是否有效
     * @param param
     * @return
     */
    public int checkDataIsValid(Map<String,String> param);


    /**
     * 批量添加未合作停车场信息
     * @param paramList
     * @return
     */
    public int addBatchNonCooperationPark(List<Map<String,Object>> paramList);

    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * @param param
     * @return
     */
    public List<Map<String,Object>> findNearbyPark(Map<String,String> param);

    /**
     * 获取所有的合作停车场列表
     * @return
     */
    public List<Map<String,Object>> findPrakList();

    /* #################################################  end小程序和网页端地图（停车场）接口  #################################################*/




    /* #################################################  start小程序接口  #################################################*/

    /**
     * 获取车辆列表
     * @param token
     * @return
     */
    public List<Map<String,Object>> getCarList(String token);

    /**
     * 我的行程
     * @param param
     *      token
     *      platenum 车牌号 为空查询所有
     * @return
     */
    public List<Map<String,Object>> getRouteList(Map<String,Object> param);

    /**
     * 添加绑定车辆
     * @param param
     *      username   手机号
     *      platenum   车牌号
     *      isactive   数字越大排列顺序越往后，数字最小的为默认车辆
     *      is_default 是否是默认车辆（0：否；1：是）
     * @return
     */
    public int insertBaseUserCar(Map<String,Object> param);

    /**
     * 根据token查询用户手机号
     * @param token
     * @return
     */
    public String getWechatUserByToken(String token);

    /**
     * 查询 某个手机号下是否有车牌存在
     * @param username 手机号
     * @return
     */
    public int getPlateNumCountByUserName(String username);

    /**
     * 获取某个手机号下最晚绑定的车牌号顺序
     * @param username 手机号
     * @return
     */
    public int getMaxIsactiveByUserName(String username);

    /**
     * 获取某个手机号下最早绑定的车牌号顺序
     * @param username 手机号
     * @return
     */
    public int getMinIsactiveByUserName(String username);

    /**
     * 修改绑定车辆信息
     * @param param
     *      SQL条件：username 手机号   platenum 车牌号
     *      修改字段(哪个字段有值修改哪个字段)：
     *          status     00为正常状态，01为绑定解除状态
     *          isactive   数字越大排列顺序越往后，数字最小的为默认车辆
     *          is_default  是否是默认车辆（0：否；1：是）
     *          is_open_unionpay  银联代扣开启状态(0为关闭，1为开启)
     *          unionpay_bind_id  银联绑定id，对应base_user_car_unionpay中的id
     *          card_type   区分银联和招商：CUP--银联；CMB--招商；NJ--南京银联;CZ--浙商
     *          limit_amount  每日代扣限额
     * @return
     */
    public int updateBaseUserCar(Map<String,Object> param);


    /* #################################################  end小程序接口  #################################################*/

}
