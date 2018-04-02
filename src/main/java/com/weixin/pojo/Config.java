package com.weixin.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/4/2.
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Config {


    /*-------------------------------------------------------------阿里大于---------------------------------------------------------------*/
    //https短信请求路劲
    private String prod_url;
     //TOP分配给应用的AppKey。
    private String app_key;
    //密钥
    private String app_secret;
    // 短信类型，传入值请填写normal
    private String sms_type;
    //短信签名，传入的短信签名必须是在阿里大于“管理中心-验证码/短信通知/推广短信-配置短信签名”中的可用签名。如“阿里大于”已在短信签名管理中通过审核，则可传入”阿里大于“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大于】欢迎使用阿里大于服务。
    private String sms_free_sign_name;
    //短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
    private String rec_num;
    //短信模板ID，传入的模板必须是在阿里大于“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
    private String sms_template_code;
    /*-------------------------------------------------------------阿里大于----------------------------------------------------------------*/


    public String getProd_url() {
        return prod_url;
    }

    public void setProd_url(String prod_url) {
        this.prod_url = prod_url;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getApp_secret() {
        return app_secret;
    }

    public void setApp_secret(String app_secret) {
        this.app_secret = app_secret;
    }

    public String getSms_type() {
        return sms_type;
    }

    public void setSms_type(String sms_type) {
        this.sms_type = sms_type;
    }

    public String getSms_free_sign_name() {
        return sms_free_sign_name;
    }

    public void setSms_free_sign_name(String sms_free_sign_name) {
        this.sms_free_sign_name = sms_free_sign_name;
    }

    public String getRec_num() {
        return rec_num;
    }

    public void setRec_num(String rec_num) {
        this.rec_num = rec_num;
    }

    public String getSms_template_code() {
        return sms_template_code;
    }

    public void setSms_template_code(String sms_template_code) {
        this.sms_template_code = sms_template_code;
    }
}
