package com.wexin.smallRoutinePay.controller;

import com.wexin.smallRoutinePay.model.WxPayParam;
import com.wexin.smallRoutinePay.util.IpUtil;
import com.wexin.smallRoutinePay.util.MapUtil;
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
    public void s(HttpServletRequest request){
        //微信下单接口
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        WxPayParam wxPayParam = new WxPayParam();
        TreeMap<String,String> param = new TreeMap<String,String>();
        param.put("appid",wxPayParam.getAppid());
        param.put("mch_id",wxPayParam.getMch_id());
        param.put("nonce_str",wxPayParam.getNonce_str());
        param.put("sign",wxPayParam.getSign());
        param.put("body",wxPayParam.getBody());
        param.put("out_trade_no",wxPayParam.getOut_trade_no());
        param.put("total_fee",wxPayParam.getTotal_fee());
        param.put("spbill_create_ip",IpUtil.getIpAddr(request));
        param.put("notify_url",wxPayParam.getNotify_url());
        param.put("trade_type",wxPayParam.getTrade_type());
        MapUtil.MapToString(param);
    }

    @RequestMapping(value = "/appPayNotifyUrl")
    public void appPayNotifyUrl(){


    }


    public static void main(String[] args) {

    }


}
