package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.weixin.dao.AutologonMapper;
import com.weixin.pojo.Config;
import com.weixin.pojo.Result;
import com.weixin.service.AutologonService;
import com.weixin.util.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/28.
 */
@Service
public class AutologonServiceImpl implements AutologonService{

    private final Logger logger = LoggerFactory.getLogger(AutologonServiceImpl.class);

    @Autowired
    private AutologonMapper autologonMapper;
    @Autowired
    private Config config;


    /**
     * 验证tonken是否存在
     * @param token
     * @return
     */
    public int getTokenCount(String token){
        logger.info("进入自动登陆service-->验证tonken是否存在,参数:["+token+"]");
        return autologonMapper.getTokenCount(token);
    }

    /**
     * 验证unionid是否存在  手机号是否绑定了unionid
     * @param unionid
     * @return
     */
    public int getUnionidCount(String unionid){
        logger.info("进入自动登陆service-->验证unionid是否存在  手机号是否绑定了unionid,参数:["+unionid+"]");
        return autologonMapper.getUnionidCount(unionid);
    }

    /**
     * 根据unionid获取token
     * @param unionid
     * @return
     */
    public String getTokenByUnionid(String unionid){
        logger.info("进入自动登陆service-->根据unionid获取token,参数:["+unionid+"]");
        return autologonMapper.getTokenByUnionid(unionid);
    }

    /**
     * 绑定手机号
     *  能到这里,说明用户肯定没有绑定过unionid
     * @param param
     * @return
     */
    public Result bindPhone(Map<String,Object> param) throws Exception{
        logger.info("进入自动登陆service-->绑定手机号,参数:["+param+"]");
        //调用微信官网接口获取unionid
        String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",param.get("code").toString());
        JSONObject json =  RequestWx.doGetWx(url);
        param.put("openid",json.get("openid"));
        if(json.get("unionid")==null||"".equals(json.get("unionid"))){
            logger.info("自动登陆service-->绑定手机号-->code无效!");
            return ResultUtil.requestFaild("code无效!");
        }
        String token  = UuidUtils.get32UUID();
        param.put("token",token);
        param.put("unionid",json.get("unionid"));
        //根据手机号获取绑定消息  绑定手机号时验证手机号是否相同
        Map<String,Object> map = autologonMapper.getBindInfoByPhone(param.get("phone").toString());
        logger.info("自动登陆service-->绑定手机号-->根据手机号获取绑定消息  绑定手机号时验证手机号是否相同,查询数据:["+map+"]");
        if(map==null){//说明是新用户
            param.put("nickname","");
            param.put("headimgurl","");
            autologonMapper.insertWechatUser(param);
            logger.info("自动登陆service-->绑定手机号-->新用户-->插入数据!");
        }else{
            autologonMapper.updateWechatUser(param);
            logger.info("自动登陆service-->绑定手机号-->老用户-->更新数据!");
        }
        return ResultUtil.requestSuccess(token);
    }

    /**
     * 获取阿里大于短信验证
     * @return
     */
    public Result getAliDaYuCheckCode(String phone) throws Exception{
        logger.info("进入自动登陆service-->获取阿里大于短信验证,参数:["+phone+"]");
        TaobaoClient client = new DefaultTaobaoClient(config.getProd_url(),config.getApp_key(),config.getApp_secret());
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        //req.setExtend("123456");
        req.setSmsType(config.getSms_type());
        req.setSmsFreeSignName(config.getSms_free_sign_name());
        String checkCode =  RandomUtils.Generate6RandomNumbers();
        req.setSmsParamString("{\"code\":\""+checkCode+"\"}");
        req.setRecNum(phone);
        req.setSmsTemplateCode(config.getSms_template_code());
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        System.out.println("-------------------------------------------");
        System.out.println(rsp.getBody());
        System.out.println("-------------------------------------------");
        JSONObject json = JSON.parseObject(rsp.getBody());
        if(json.get("alibaba_aliqin_fc_sms_num_send_response")!=null){
            logger.info("自动登陆service-->获取阿里大于短信验证-->成功,验证码:["+checkCode+"]");
            JSONObject json1 = JSON.parseObject(json.get("alibaba_aliqin_fc_sms_num_send_response").toString());
            JSONObject json2 = JSON.parseObject(json1.get("result").toString());
            //if(json2.get("msg").toString().equals("OK")){
                return ResultUtil.requestSuccess(checkCode);
            //}
        }else{
            JSONObject json1 = JSON.parseObject(json.get("error_response").toString());
            logger.info("自动登陆service-->获取阿里大于短信验证-->失败,消息:["+json1.get("sub_msg").toString()+"]");
            return ResultUtil.requestFaild(json1.get("sub_msg").toString());
        }
    }

    /**
     * 微信小程序授权登陆
     * @param param
     * @return
     */
    public Result authorizationLogin(Map<String,Object> param) throws Exception {
        logger.info("进入自动登陆service-->微信小程序授权登陆,参数:["+param+"]");
        String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",param.get("code").toString());
        JSONObject json =  RequestWx.doGetWx(url);
        param.put("openid",json.get("openid"));
        if(json.get("unionid")==null||"".equals(json.get("unionid"))){
            logger.info("自动登陆service-->微信小程序授权登陆-->code无效!");
            return ResultUtil.requestFaild("code无效!");
        }
        String token  = UuidUtils.get32UUID();
        param.put("token",token);
        param.put("unionid",json.get("unionid"));
        //根据手机号获取绑定消息  绑定手机号时验证手机号是否相同
        Map<String,Object> map = autologonMapper.getBindInfoByPhone(param.get("phone").toString());
        logger.info("自动登陆service-->微信小程序授权登陆-->根据手机号获取绑定消息  绑定手机号时验证手机号是否相同,返回数据:["+map+"]");
        if(map==null){//说明是新用户
            param.put("nickname","");
            param.put("headimgurl","");
            autologonMapper.insertWechatUser(param);
            logger.info("自动登陆service-->微信小程序授权登陆-->新用户-->插入数据!");
        }else{
            autologonMapper.updateWechatUser(param);
            logger.info("自动登陆service-->微信小程序授权登陆-->新用户-->更新数据!");

        }
        return ResultUtil.requestSuccess(token);
    }


}
