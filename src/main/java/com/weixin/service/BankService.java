package com.weixin.service;

import com.weixin.pojo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
public interface BankService {

    /**
     * 根据银行卡号 获取  所属银行名称
     * @param param
     *      {"token":"","card":""}
     *      card:银行卡号
     * @return
     */
    public Result getCardByBankName(Map<String,Object> param) throws Exception;

    /**
     * 获取 银行活动 列表
     * @return
     */
    public Result getBankActivitList() throws Exception;

    /**
     * 绑卡 获取阿里大于短信验证
     * @param phone  手机号
     * @param cardnum  银行卡号
     * @return
     * @throws Exception
     */
    public Result getAliDaYuCheckCode(String phone,String cardnum) throws Exception;


    /**
     * 绑定银行卡
     * @param param
     *      {"token":"","platenum":"","cardnum":"","name":"","username":"","checkcode":""}
     *      platenum : 车牌号
     *      cardnum : 银行卡号
     *      name : 姓名
     *      username : 手机号
     *      checkcode : 手机验证码
     * @return
     * @throws Exception
     */
    public Result bindBankCard(Map<String,Object> param, HttpServletRequest req, HttpServletResponse resp) throws Exception;


    /**
     * 解绑银行卡
     * @param param
     *      {"token":"","platenum":""}
     *      platenum : 车牌号
     * @param req
     * @param resp
     * @return
     */
    public Result UnBindBankCard(Map<String,Object> param, HttpServletRequest req, HttpServletResponse resp) throws Exception;


}
