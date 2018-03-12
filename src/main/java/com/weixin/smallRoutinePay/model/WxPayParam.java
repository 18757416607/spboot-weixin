package com.weixin.smallRoutinePay.model;

/**
 * Created by Administrator on 2018/3/1.
 * 下单参数类
 */
public class WxPayParam {

    //微信下单接口
    String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //小程序appid
    public String appid = "wx96fa49493e152caf";
    //微信支付的商户id
    public String mch_id = "1253517601";
    //微信api密钥
    public String api_key = "cabintechwxpayapi20170627gujia12";
    //随机字符串
    public String nonce_str = "";
    //签名
    public String sign = "";
    //签名方式，固定值
    public String sign_type = "MD5";
    //商品描述
    public String body = "停车费用";
    //商户订单号
    public String out_trade_no = "";
    //标价金额
    public String total_fee = "";
    //终端IP
    public String spbill_create_ip = "";
    //通知地址
    public String  notify_url = "https://mp.weixin.qq.com/weixin/appPay/appPayNotifyUrl";
    //交易类型，小程序支付的固定值为JSAPI
    public String trade_type = "JSAPI";


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
