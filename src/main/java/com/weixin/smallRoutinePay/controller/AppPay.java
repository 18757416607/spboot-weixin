package com.weixin.smallRoutinePay.controller;

import com.weixin.smallRoutinePay.model.WxPayParam;
import com.weixin.smallRoutinePay.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/3/1.
 * 小程序支付
 */
@Controller
@RequestMapping(value = "/appPay")
public class AppPay {

    @RequestMapping(value = "/tt")
    public void s(HttpServletRequest request) throws  Exception{
        //微信下单接口
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        WxPayParam wxPayParam = new WxPayParam();
        TreeMap<String,String> param = new TreeMap<String,String>();

        param.put("appid",wxPayParam.getAppid());
        param.put("mch_id",wxPayParam.getMch_id());
        param.put("nonce_str", UuidUtil.get32UUID());
        param.put("body",wxPayParam.getBody());
        param.put("out_trade_no",UuidUtil.get32UUID());
        param.put("total_fee","1");
        param.put("spbill_create_ip",IpUtil.getIpAddr(request));
        param.put("notify_url",wxPayParam.getNotify_url());
        param.put("trade_type",wxPayParam.getTrade_type());
        param.put("openid",request.getParameter("openid"));
        String str = MapXmlUtil.MapToString(param)+"&key="+wxPayParam.getApi_key();
        String sign = MD5.getMD5(str).toUpperCase();
        param.put("sign",sign);
        String xmlParam = MapXmlUtil.mapToXml(param);
        xmlParam = xmlParam.substring(xmlParam.lastIndexOf("?")+2,xmlParam.length());
        String result = UrlConnectUtil.httpsPost(url,xmlParam);
        System.out.println(result);
    }

    @RequestMapping(value = "/appPayNotifyUrl")
    public void appPayNotifyUrl(){


    }


}
