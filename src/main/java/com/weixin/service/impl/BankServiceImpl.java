package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.weixin.dao.BankMapper;
import com.weixin.dao.ParkingMapper;
import com.weixin.pojo.BaseUserCarBindUnionpay;
import com.weixin.pojo.BaseUserCarUnionpay;
import com.weixin.pojo.Config;
import com.weixin.pojo.Result;
import com.weixin.service.BankService;
import com.weixin.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@Service
public class BankServiceImpl implements BankService{

    private final static Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private Config config;

    /**
     * 根据银行卡号 获取  所属银行名称
     * @param param
     *         {"token":"","card":""}
     *         card:银行卡号
     * @return
     *        -1：系统报错   00：成功   01：参数为空  02：请输入正确的银行卡号
     */
    public Result getCardByBankName(Map<String,Object> param) throws Exception{
        logger.info("进入根据银行卡号 获取  所属银行名称 service");
        String cardbin = param.get("cardnum").toString().substring(0,6);
        logger.info("根据银行卡号 获取  所属银行名称 -->前六位卡bin:"+cardbin);
        Map<String,Object> bankInfo = bankMapper.getCardByBankName(cardbin);
        if(bankInfo==null){
            return ResultUtil.requestSuccess("请输入正确的银行卡号","请输入正确的银行卡号","02");
        }
        logger.info("根据银行卡号 获取  所属银行名称 -->获取的银行名称:"+bankInfo);
        String bankName = bankInfo.get("card_bank").toString() + bankInfo.get("card_type").toString();
        return ResultUtil.requestSuccess(bankName);
    }


    /**
     * 获取 银行活动 列表
     * @return
     */
    public Result getBankActivitList() throws Exception{
        logger.info("进入获取 银行活动 列表 service");
        List<Map<String,Object>> bankActivitList = bankMapper.getBankActivitList();
        logger.info("根据获取 银行活动 列表 --> 数据:"+bankActivitList);
        return ResultUtil.requestSuccess(JSON.toJSON(bankActivitList).toString());
    }


    /**
     * 绑卡 获取阿里大于短信验证
     * @param phone  手机号
     * @param cardnum  银行卡号
     * @return
     *      -1：系统报错   00：成功   01：参数为空  02：阿里大与返回的消息
     * @throws Exception
     */
    public Result getAliDaYuCheckCode(String phone,String cardnum) throws Exception{
        logger.info("进入 绑卡 获取阿里大于短信验证-->获取阿里大于短信验证service");
        String cardBin = cardnum.substring(0,6);  //获取用户输入卡号前6位
        TaobaoClient client = new DefaultTaobaoClient(config.getProd_url(),config.getApp_key(),config.getApp_secret());
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        //req.setExtend("123456");
        req.setSmsType(config.getSms_type());
        req.setSmsFreeSignName(config.getSms_free_sign_name());
        String checkCode =  RandomUtils.Generate4RandomNumbers();
        //${msg}，正在开通代扣服务，10分钟内输入有效！L
        req.setSmsParamString("{\"msg\":\""+checkCode+"(验证码),尾号"+cardnum.substring(cardnum.length()-4,cardnum.length())+"\"}");
        req.setRecNum(phone);
        req.setSmsTemplateCode("SMS_133550042");
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println("-------------------------------------------");
        System.out.println(rsp.getBody());
        System.out.println("-------------------------------------------");
        logger.info("绑卡 获取阿里大于短信验证-->阿里大与返回数据:["+rsp.getBody()+"]");
        JSONObject json = JSON.parseObject(rsp.getBody());
        if(json.get("alibaba_aliqin_fc_sms_num_send_response")!=null){
            JSONObject json1 = JSON.parseObject(json.get("alibaba_aliqin_fc_sms_num_send_response").toString());
            JSONObject json2 = JSON.parseObject(json1.get("result").toString());
            if(json2.get("msg").toString().equals("OK")){
                //设置验证码到redis中
                Jedis jedis = RedisClient.getJedis();
                jedis.setex("xcx"+phone,600, checkCode);
                RedisClient.returnResource(jedis);
                logger.info("绑卡 获取阿里大于短信验证-->验证码获取成功,验证码:["+checkCode+"]");
                return ResultUtil.requestSuccess("验证码获取成功");
            }else{
                return ResultUtil.requestSuccess(json2.get("sub_msg").toString(),"02");
            }
        }else{
            JSONObject json1 = JSON.parseObject(json.get("error_response").toString());
            logger.info("绑卡 获取阿里大于短信验证-->验证码获取失败-->返回数据["+json1.get("sub_msg").toString()+"]");
            return ResultUtil.requestSuccess(json1.get("sub_msg").toString(),json1.get("sub_msg").toString(),"02");
        }
    }


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
     *      -1：系统报错   00：成功  01：参数为空  02：表更新不成功   03：验证码失效,请重新获取验证码!  04:验证码错误!   05:银联返回的消息  06:车辆已有绑定银行卡
     * @throws Exception
     */
    @Transactional
    public Result bindBankCard(Map<String,Object> param, HttpServletRequest req, HttpServletResponse resp) throws Exception{
        logger.info("进入小程序绑定银行卡service");
        //获取redis中的验证码
        /*Jedis jedis = RedisClient.getJedis();
        System.out.println("redis取出验证码："+jedis.get("xcx"+param.get("username").toString()));
        logger.info("小程序绑定银行卡-->前端-redis-key:"+jedis.get("xcx"+param.get("username").toString()));
        if(jedis.get("xcx"+param.get("username").toString())==null){
            logger.info("小程序绑定银行卡-->验证码失效,请重新获取验证码!");
            return ResultUtil.requestSuccess("验证码失效,请重新获取验证码!","验证码失效,请重新获取验证码!","03");
        }
        if(!jedis.get("xcx"+param.get("username").toString()).equals(param.get("checkcode"))){
            logger.info("小程序绑定银行卡-->验证码错误!");
            return ResultUtil.requestSuccess("验证码错误!","验证码错误!","04");
        }*/

        String unionId = bankMapper.getNewBindTableCardNumByPlateNum(param.get("platenum").toString());
        if(unionId!=null&&!"".equals(unionId)){
            return ResultUtil.requestSuccess("["+param.get("platenum")+"]该车辆下已有绑定银行卡","["+param.get("platenum")+"]该车辆下已有绑定银行卡","06");
        }

        String cardBin = param.get("cardnum").toString().substring(0,6);  //获取用户输入卡号前6位
        String cardnumlast = param.get("cardnum").toString().substring(param.get("cardnum").toString().length()-4,param.get("cardnum").toString().length());
        param.put("id","");
        Map<String,Object> bankInfo = bankMapper.getCardByBankName(cardBin); //根据银行卡号前六位 获取  所属银行名称
        //添加 绑卡信息  新表
        BaseUserCarBindUnionpay baseUserCarBindUnionpay = new BaseUserCarBindUnionpay();
        baseUserCarBindUnionpay.setPlate_num(param.get("platenum").toString());
        baseUserCarBindUnionpay.setCard_bin(cardBin);
        baseUserCarBindUnionpay.setCard_num_last(cardnumlast);
        baseUserCarBindUnionpay.setUser_name(param.get("username").toString());
        baseUserCarBindUnionpay.setCard_bank(bankInfo.get("card_bank").toString());
        baseUserCarBindUnionpay.setCard_type(bankInfo.get("card_type").toString());
        int count = bankMapper.insertBaseUserCarBindUnionPay(baseUserCarBindUnionpay);
        logger.info("小程序绑定银行卡-->添加 绑卡信息  新表-->受影响行数:["+count+"]");
        if(count>0){
            //调用银联绑卡接口
            BindCardUtil bindCardUtil = new BindCardUtil();
            param.put("bindid",baseUserCarBindUnionpay.getId().toString());
            Result result = bindCardUtil.requestBindCard(req,resp,param);
            logger.info("小程序绑定银行卡-->调用银联绑卡接口-->返回数据:["+result.getData()+"]");
            if(result.getCode().equals("00")){
                //添加 绑卡信息  旧表
                BaseUserCarUnionpay baseUserCarUnionpay = new BaseUserCarUnionpay();
                baseUserCarUnionpay.setUser_name(param.get("username").toString());
                baseUserCarUnionpay.setPlate_num(param.get("platenum").toString());
                baseUserCarUnionpay.setCard_num(param.get("cardnum").toString());
                baseUserCarUnionpay.setReal_name(param.get("name").toString());
                baseUserCarUnionpay.setPhone_num(param.get("username").toString());
                baseUserCarUnionpay.setCard_bank(bankInfo.get("card_bank").toString());
                baseUserCarUnionpay.setCard_type(bankInfo.get("card_type").toString());
                int count1 = bankMapper.insertBaseUserCarUnionPay(baseUserCarUnionpay);
                logger.info("小程序绑定银行卡-->添加 绑卡信息  旧表-->受影响行数:["+count1+"]");
                if(count1>0){
                    param.put("unionpay_bind_id",baseUserCarBindUnionpay.getId());
                    int count2 = bankMapper.updateBaseUserCar(param);  //绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id
                    logger.info("小程序绑定银行卡-->绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id-->受影响行数:["+count2+"]");
                    logger.info("小程序绑定银行卡-->绑卡成功");
                    return ResultUtil.requestSuccess("绑卡成功");
                    /*if(count2>0){
                        logger.info("小程序绑定银行卡-->绑卡成功");
                        return ResultUtil.requestSuccess("绑卡成功");
                    }else{
                        logger.info("小程序绑定银行卡-->绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id 受影响行数0行");
                        return ResultUtil.requestSuccess("绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id 受影响行数0行","绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id 受影响行数0行","02");
                    }*/
                    /*int platenumCount = bankMapper.getIsBindCar(param.get("platenum").toString()); //判断某个车牌是否被绑定
                    if(platenumCount==0){  //车牌号没有被绑定
                        param.put("unionpaybindid",baseUserCarBindUnionpay.getId());
                        int count2 = bankMapper.insertBaseUserCar(param);
                        if(count2>0){
                            return ResultUtil.requestSuccess("绑车且绑卡成功");
                        }else{
                            return ResultUtil.requestSuccess("添加绑车表受影响行数0行","添加绑卡旧表受影响行数0行","01");
                        }
                    }else{
                        return ResultUtil.requestSuccess("绑卡成功");
                    }*/
                }else{
                    logger.info("小程序绑定银行卡-->添加绑卡旧表受影响行数0行");
                    return ResultUtil.requestSuccess("添加绑卡旧表受影响行数0行","添加绑卡旧表受影响行数0行","02");
                }
            }else{
                //由于先往绑卡信息表（新表）中插入一条数据, 进入这个else说明用户输入的三要求（姓名，手机号，银行卡号）不匹配
                logger.info("小程序绑定银行卡-->用户输入的三要求（姓名，手机号，银行卡号）不匹配");
                bankMapper.deleteBaseUserCarBindUnionPay(baseUserCarBindUnionpay.getId());
                return ResultUtil.requestSuccess(result.getMsg(),result.getMsg(),"05");
            }
        }else{
            logger.info("小程序绑定银行卡-->添加绑卡新表受影响行数0行");
            return ResultUtil.requestSuccess("添加绑卡新表受影响行数0行","添加绑卡新表受影响行数0行","02");
        }
    }


    /**
     * 解绑银行卡
     * @param param
     *      {"token":"","platenum":""}
     *      platenum : 车牌号
     * @param req
     * @param resp
     * @return
     *      -1：系统报错   00：成功  01：参数为空  02：表更新不成功 03:车牌号对应的银行卡号不存在
     */
    @Transactional
    public Result UnBindBankCard(Map<String,Object> param, HttpServletRequest req, HttpServletResponse resp) throws Exception{
        logger.info("进入小程序解绑银行卡service");
        String cardNum = bankMapper.getOldBindTableCardNumByPlateNum(param); //根据 车牌号 获取 绑卡旧表中的银行卡号
        if(cardNum!=null&&!"".equals(cardNum)){
            UnBindCardUtil unBindCardUtil = new UnBindCardUtil();
            Map<String,Object> unionpayMap = new HashMap<String,Object>();
            String bindId = bankMapper.getNewBindTableCardNumByPlateNum(param.get("platenum").toString());  //解绑银行卡时 需要获取绑卡时上送的bindId
            unionpayMap.put("cardNum",cardNum);
            unionpayMap.put("bindId",bindId);
            Result result = unBindCardUtil.requestUnBindCard(req,resp,unionpayMap);
            if(result.getCode().equals("00")){
                int newCount = bankMapper.updateBaseUserCarBindUnionPay(param);  //更新 绑卡 新表信息
                if(newCount>0){
                    int oldCount = bankMapper.updateBaseUserCarUnionpay(param);      //更新 绑卡 旧表信息
                    if(oldCount>0){
                        logger.info("小程序解绑银行卡service-->解绑成功");
                        return ResultUtil.requestSuccess("解绑成功");
                    }else{
                        logger.info("小程序解绑银行卡service-->更新绑卡旧表受影响行数0行");
                        return ResultUtil.requestSuccess("更新绑卡旧表受影响行数0行","更新绑卡新表受影响行数0行","02");
                    }
                }else{
                    logger.info("小程序解绑银行卡service-->更新绑卡新表受影响行数0行");
                    return ResultUtil.requestSuccess("更新绑卡新表受影响行数0行","更新绑卡新表受影响行数0行","02");
                }
            }else{
                logger.info("小程序解绑银行卡service-->解绑失败");
                return ResultUtil.requestFaild("解绑失败");
            }
        }else{
            logger.info("小程序解绑银行卡service--车牌号对应的银行卡号不存在");
            return ResultUtil.requestSuccess("车牌号对应的银行卡号不存在","车牌号对应的银行卡号不存在","03");
        }
    }


    /**
     * 切换绑定银行卡
     * @param param
     *      {"token":"","platenum":"","cardnum":"","name":"","username":"","checkcode":""}
     *      platenum : 车牌号
     *      cardnum : 银行卡号
     *      name : 姓名
     *      username : 手机号
     *      checkcode : 手机验证码
     * @return
     *      -1：系统报错   00：成功  01：参数为空  02：表更新不成功   03：验证码失效,请重新获取验证码!  04:验证码错误!   05:银联返回的消息  06:车牌还未绑定卡号
     * @throws Exception
     */
    @Transactional
    public Result changeBindCard(Map<String,Object> param, HttpServletRequest req, HttpServletResponse resp) throws Exception{

        logger.info("进入小程切换绑定银行卡service");
        //获取redis中的验证码
        Jedis jedis = RedisClient.getJedis();
        System.out.println("redis取出验证码："+jedis.get("xcx"+param.get("username").toString()));
        logger.info("小程切换绑定银行卡-->前端-redis-key:"+jedis.get("xcx"+param.get("username").toString()));
        if(jedis.get("xcx"+param.get("username").toString())==null){
            logger.info("小程切换绑定银行卡-->验证码失效,请重新获取验证码!");
            return ResultUtil.requestSuccess("验证码失效,请重新获取验证码!","验证码失效,请重新获取验证码!","03");
        }
        if(!jedis.get("xcx"+param.get("username").toString()).equals(param.get("checkcode"))){
            logger.info("小程切换绑定银行卡-->验证码错误!");
            return ResultUtil.requestSuccess("验证码错误!","验证码错误!","04");
        }


        String cardNum = bankMapper.getOldBindTableCardNumByPlateNum(param); //根据 车牌号 获取 绑卡旧表中的银行卡号
        if(cardNum!=null&&!"".equals(cardNum)){
            UnBindCardUtil unBindCardUtil = new UnBindCardUtil();
            Map<String,Object> unionpayMap = new HashMap<String,Object>();
            String bindId = bankMapper.getNewBindTableCardNumByPlateNum(param.get("platenum").toString());  //解绑银行卡时 需要获取绑卡时上送的bindId
            unionpayMap.put("cardNum",cardNum);
            unionpayMap.put("bindId",bindId);
            Result result = unBindCardUtil.requestUnBindCard(req,resp,unionpayMap);
            if(result.getCode().equals("00")){
                int newCount = bankMapper.updateBaseUserCarBindUnionPay(param);  //更新 绑卡 新表信息
                if(newCount>0){
                    int oldCount = bankMapper.updateBaseUserCarUnionpay(param);      //更新 绑卡 旧表信息
                    if(oldCount>0){
                        logger.info("小程序解绑银行卡service-->解绑成功");
                    }else{
                        logger.info("小程序解绑银行卡service-->更新绑卡旧表受影响行数0行");
                        return ResultUtil.requestSuccess("更新绑卡旧表受影响行数0行","更新绑卡新表受影响行数0行","02");
                    }
                }else{
                    logger.info("小程序解绑银行卡service-->更新绑卡新表受影响行数0行");
                    return ResultUtil.requestSuccess("更新绑卡新表受影响行数0行","更新绑卡新表受影响行数0行","02");
                }
            }else{
                logger.info("小程序解绑银行卡service-->解绑失败");
                return ResultUtil.requestFaild("解绑失败");
            }
        }else{
            logger.info("小程序解绑银行卡service--> ["+param.get("platenum")+"]车牌还未绑定卡号");
            return ResultUtil.requestSuccess("["+param.get("platenum")+"]车牌还未绑定卡号","["+param.get("platenum")+"]车牌还未绑定卡号","03");
        }




        String cardBin = param.get("cardnum").toString().substring(0,6);  //获取用户输入卡号前6位
        String cardnumlast = param.get("cardnum").toString().substring(param.get("cardnum").toString().length()-4,param.get("cardnum").toString().length());
        param.put("id","");
        Map<String,Object> bankInfo = bankMapper.getCardByBankName(cardBin); //根据银行卡号前六位 获取  所属银行名称
        //添加 绑卡信息  新表
        BaseUserCarBindUnionpay baseUserCarBindUnionpay = new BaseUserCarBindUnionpay();
        baseUserCarBindUnionpay.setPlate_num(param.get("platenum").toString());
        baseUserCarBindUnionpay.setCard_bin(cardBin);
        baseUserCarBindUnionpay.setCard_num_last(cardnumlast);
        baseUserCarBindUnionpay.setUser_name(param.get("username").toString());
        baseUserCarBindUnionpay.setCard_bank(bankInfo.get("card_bank").toString());
        baseUserCarBindUnionpay.setCard_type(bankInfo.get("card_type").toString());
        int count = bankMapper.insertBaseUserCarBindUnionPay(baseUserCarBindUnionpay);
        logger.info("小程切换绑定银行卡-->添加 绑卡信息  新表-->受影响行数:["+count+"]");
        if(count>0){
            //调用银联绑卡接口
            BindCardUtil bindCardUtil = new BindCardUtil();
            param.put("bindid",baseUserCarBindUnionpay.getId().toString());
            Result result = bindCardUtil.requestBindCard(req,resp,param);
            logger.info("小程切换绑定银行卡-->调用银联绑卡接口-->返回数据:["+result.getData()+"]");
            if(result.getCode().equals("00")){
                //添加 绑卡信息  旧表
                BaseUserCarUnionpay baseUserCarUnionpay = new BaseUserCarUnionpay();
                baseUserCarUnionpay.setUser_name(param.get("username").toString());
                baseUserCarUnionpay.setPlate_num(param.get("platenum").toString());
                baseUserCarUnionpay.setCard_num(param.get("cardnum").toString());
                baseUserCarUnionpay.setReal_name(param.get("name").toString());
                baseUserCarUnionpay.setPhone_num(param.get("username").toString());
                baseUserCarUnionpay.setCard_bank(bankInfo.get("card_bank").toString());
                baseUserCarUnionpay.setCard_type(bankInfo.get("card_type").toString());
                int count1 = bankMapper.insertBaseUserCarUnionPay(baseUserCarUnionpay);
                logger.info("小程切换绑定银行卡-->添加 绑卡信息  旧表-->受影响行数:["+count1+"]");
                if(count1>0){
                    param.put("unionpay_bind_id",baseUserCarBindUnionpay.getId());
                    int count2 = bankMapper.updateBaseUserCar(param);  //绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id
                    logger.info("小程切换绑定银行卡-->绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id-->受影响行数:["+count2+"]");
                    if(count2>0){
                        logger.info("小程切换绑定银行卡-->绑卡成功");
                        return ResultUtil.requestSuccess("绑卡成功");
                    }else{
                        logger.info("小程切换绑定银行卡-->绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id 受影响行数0行");
                        return ResultUtil.requestSuccess("绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id 受影响行数0行","绑卡成功后  修改银联代扣开关为开启   unionpay_bind_id 里插入绑卡新表的id 受影响行数0行","02");
                    }
                    /*int platenumCount = bankMapper.getIsBindCar(param.get("platenum").toString()); //判断某个车牌是否被绑定
                    if(platenumCount==0){  //车牌号没有被绑定
                        param.put("unionpaybindid",baseUserCarBindUnionpay.getId());
                        int count2 = bankMapper.insertBaseUserCar(param);
                        if(count2>0){
                            return ResultUtil.requestSuccess("绑车且绑卡成功");
                        }else{
                            return ResultUtil.requestSuccess("添加绑车表受影响行数0行","添加绑卡旧表受影响行数0行","01");
                        }
                    }else{
                        return ResultUtil.requestSuccess("绑卡成功");
                    }*/
                }else{
                    logger.info("小程切换绑定银行卡-->添加绑卡旧表受影响行数0行");
                    return ResultUtil.requestSuccess("添加绑卡旧表受影响行数0行","添加绑卡旧表受影响行数0行","02");
                }
            }else{
                //由于先往绑卡信息表（新表）中插入一条数据, 进入这个else说明用户输入的三要求（姓名，手机号，银行卡号）不匹配
                logger.info("小程切换绑定银行卡-->用户输入的三要求（姓名，手机号，银行卡号）不匹配");
                bankMapper.deleteBaseUserCarBindUnionPay(baseUserCarBindUnionpay.getId());
                return ResultUtil.requestSuccess(result.getMsg(),result.getMsg(),"05");
            }
        }else{
            logger.info("小程切换绑定银行卡-->添加绑卡新表受影响行数0行");
            return ResultUtil.requestSuccess("添加绑卡新表受影响行数0行","添加绑卡新表受影响行数0行","02");
        }
    }



}
