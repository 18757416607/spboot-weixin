package com.weixin.smallRoutinePay.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixin.pojo.Result;
import com.weixin.smallRoutinePay.dao.AppPayMapper;
import com.weixin.smallRoutinePay.model.WxPayParam;
import com.weixin.smallRoutinePay.service.AppPayService;
import com.weixin.smallRoutinePay.util.*;
import com.weixin.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/3/12.
 */
@Service
public class AppPayServiceImpl implements AppPayService{

    @Autowired
    private AppPayMapper appPayMapper;

    /**
     * 停车费用
     * @param paramMap
     * @return
     */
    public Result parkStopCost(Map paramMap) throws  Exception{
        String openid = paramMap.get("openid").toString();     //微信openid
        String platenum = paramMap.get("platenum").toString();  //车牌号
        boolean flag = true;  //标识是否使用了优惠卷
        if(paramMap.get("couponOwner")!=null||!paramMap.get("couponOwner").equals("")){
            String couponId = couponId = paramMap.get("couponOwner").toString();  //优惠卷类型
            int count = appPayMapper.findCouponIsExist(paramMap);
            if(count>0){

            }else{
                return ResultUtil.requestFaild("此优惠卷不存在");
            }
        }else{

        }




        WxPayParam wxPayParam = new WxPayParam();
        TreeMap<String,String> param = new TreeMap<String,String>();
        String nonce_str = UuidUtil.get32UUID();
        param.put("appid",wxPayParam.getAppid());
        param.put("mch_id",wxPayParam.getMch_id());
        param.put("nonce_str", nonce_str);
        param.put("body",wxPayParam.getBody());
        param.put("out_trade_no",UuidUtil.get32UUID());
        param.put("total_fee","1");
        param.put("spbill_create_ip", IpUtil.DNSdomainNameResolutionIp());
        param.put("notify_url",wxPayParam.getNotify_url());
        param.put("trade_type",wxPayParam.getTrade_type());
        param.put("openid",openid);
        String str = MapXmlUtil.MapToString(param)+"&key="+wxPayParam.getApi_key();
        String sign = MD5.getMD5(str).toUpperCase();
        param.put("sign",sign);
        String xmlParam = MapXmlUtil.mapToXml(param);
        xmlParam = xmlParam.substring(xmlParam.lastIndexOf("?")+2,xmlParam.length());
        String result = UrlConnectUtil.httpsPost(wxPayParam.getUrl(),xmlParam);

        Map<String,String> resultMap = MapXmlUtil.xmlToMap(result);
        StringBuffer againXml = new StringBuffer(); //小程序支付这里的签名方式不是按照把集合排序的来,按照官网提供的API下面的示例来
        String tempStamp = Integer.toString(DateUtil.getSecondTimestamp(new Date()));
        String nonceStr = UuidUtil.get32UUID();
        againXml.append("appId=").append(wxPayParam.getAppid()).append("&nonceStr=").append(nonceStr);
        againXml.append("&package=").append("prepay_id="+resultMap.get("prepay_id")).append("&signType=").append("MD5");
        againXml.append("&timeStamp=").append(tempStamp).append("&key=").append(wxPayParam.getApi_key());
        String againSign = MD5.getMD5(againXml.toString()).toUpperCase();

        Map<String,String> rtnMap = new HashMap<String,String>();
        rtnMap.put("appId",wxPayParam.getAppid());
        rtnMap.put("nonceStr",nonceStr);
        rtnMap.put("package","prepay_id="+resultMap.get("prepay_id"));
        rtnMap.put("signType","MD5");
        rtnMap.put("timeStamp",tempStamp);
        return ResultUtil.requestSuccess(JSON.toJSON(rtnMap).toString());
    }

}
