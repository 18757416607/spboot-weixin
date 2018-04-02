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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2018/3/28.
 */
@Service
public class AutologonServiceImpl implements AutologonService{

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
        return autologonMapper.getTokenCount(token);
    }

    /**
     * 验证unionid是否存在  手机号是否绑定了unionid
     * @param unionid
     * @return
     */
    public int getUnionidCount(String unionid){
        return autologonMapper.getUnionidCount(unionid);
    }

    /**
     * 根据unionid获取token
     * @param unionid
     * @return
     */
    public String getTokenByUnionid(String unionid){
        return autologonMapper.getTokenByUnionid(unionid);
    }

    /**
     * 绑定手机号
     *  能到这里,说明用户肯定没有绑定过unionid
     * @param param
     * @return
     */
    public String bindPhone(Map<String,Object> param) throws Exception{
        Map<String,Object> map = autologonMapper.getBindInfoByPhone(param.get("phone").toString());
        String token  = UuidUtils.get32UUID();
        int count = 0;
        if(map==null){  //说明是新用户
            param.put("token",token);
            count = autologonMapper.insertWechatUser(param);
        }else{
            count = autologonMapper.updateWechatUser(param);
        }
        return token;
    }

    /**
     * 获取阿里大于短信验证
     * @return
     */
    public Result getAliDaYuCheckCode(String phone) throws Exception{
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
        JSONObject json = JSON.parseObject(rsp.getBody());
        JSONObject json1 = JSON.parseObject(json.get("alibaba_aliqin_fc_sms_num_send_response").toString());
        JSONObject json2 = JSON.parseObject(json1.get("result").toString());
        if(json2.get("msg").toString().equals("OK")){
            return ResultUtil.requestSuccess(checkCode);
        }
        return ResultUtil.requestFaild(json2.get("msg").toString());
    }



}
