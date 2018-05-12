package com.weixin.dao;

import com.weixin.pojo.BaseUserCarBindUnionpay;
import com.weixin.pojo.BaseUserCarUnionpay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@Repository
@Mapper
public interface BankMapper {


    /**
     * 根据银行卡号前六位 获取  所属银行名称
     * @param cardbin 行卡号前六位
     * @return
     */
    public Map<String,Object> getCardByBankName(String cardbin);

    /**
     * 获取 银行活动 列表
     * @return
     */
    public List<Map<String,Object>> getBankActivitList();

    /**
     * 添加 绑卡信息  新表
     * @param baseUserCarBindUnionpay
     * @return
     */
    public int insertBaseUserCarBindUnionPay(BaseUserCarBindUnionpay baseUserCarBindUnionpay);

    /**
     * 解绑银行卡时 需要获取绑卡时上送的bindId
     * @param platenum  车牌号
     * @return
     */
    public String getNewBindTableCardNumByPlateNum(String platenum);

    /**
     * 删除 绑卡信息  新表
     * @param id 主键id
     * @return
     */
    public int deleteBaseUserCarBindUnionPay(int id);

    /**
     * 更新 绑卡 新表信息
     * @param param
     *      {"token":"","platenum":""}
     *      platenum : 车牌号
     * @return
     */
    public int updateBaseUserCarBindUnionPay(Map<String,Object> param);

    /**
     * 添加 绑卡信息  旧表
     * @param baseUserCarUnionpay
     * @return
     */
    public int insertBaseUserCarUnionPay(BaseUserCarUnionpay baseUserCarUnionpay);

    /**
     * 根据 车牌号 获取 绑卡旧表中的银行卡号
     * @param param
     *      {"token":"","platenum":""}
     *      platenum : 车牌号
     * @return
     */
    public Map<String,Object> getOldBindTableCardNumByPlateNum(Map<String,Object> param);


    /**
     * 更新 绑卡 旧表信息
     * @param param
     *      {"token":"","platenum":""}
     *      platenum : 车牌号
     * @return
     */
    public int updateBaseUserCarUnionpay(Map<String,Object> param);

    /**
     * 判断某个车牌是否被绑定
     * @param platenum  车牌号
     * @return
     */
    public int getIsBindCar(String platenum);

    /**
     * 添加绑车信息
     * @param param
     * @return
     */
    public int insertBaseUserCar(Map<String,Object> param);

    /**
     * 绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id
     * @param param
     *      unionpay_bind_id : 银联绑定id，对应base_user_car_unionpay中的id
     *      platenum : 车牌号
     * @return
     */
    public int updateBaseUserCar(Map<String,Object> param);

}
