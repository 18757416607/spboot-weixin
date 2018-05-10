package com.weixin.util;

import com.weixin.pojo.Result;
import com.weixin.util.acp.demo.DemoBase;
import com.weixin.util.acp.sdk.AcpService;
import com.weixin.util.acp.sdk.CertUtil;
import com.weixin.util.acp.sdk.LogUtil;
import com.weixin.util.acp.sdk.SDKConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/10.
 */
public class BindCardUtil {

    /**
     * 请求银联绑定卡片接口
     * @param req
     * @param resp
     * @param param
     * @return
     * @throws Exception
     */
    public Result requestBindCard(HttpServletRequest req, HttpServletResponse resp, Map<String,Object> param) throws Exception{
        SDKConfig.getConfig().loadPropertiesFromSrc(); //从classpath加载acp_sdk.properties文件
        /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData = AcpService.sign(setFormDate(param),DemoBase.encoding);  			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();   				//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData,requestBackUrl,DemoBase.encoding);		//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, DemoBase.encoding)){
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode") ;
                if(("00").equals(respCode)){
                    return ResultUtil.requestSuccess(null);
                }else{
                    //其他应答码为失败请排查原因或做失败处理
                    //TODO
                    return ResultUtil.requestFaild(rspData.get("respMsg"));
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因
                return ResultUtil.requestFaild("验证签名失败");
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            return ResultUtil.requestFaild("未获取到返回报文或返回http状态码非200");
        }
        /*String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);
        resp.getWriter().write("请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");*/
    }


    /**
     * 设置请求参数
     * @return 请求参数MAP
     */
    private static Map<String, String> setFormDate(Map<String,Object> param) {
        Map<String, String> data = new HashMap<String, String>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", DemoBase.version);                  //版本号
        data.put("encoding", DemoBase.encoding);                //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "72");                              //交易类型 11-代收
        data.put("txnSubType", "01");                           //交易子类型  01:需要验证绑定关系  02：免验建立绑定关系
        data.put("bizType", "000501");                          //业务类型 代收产品
        data.put("channelType", "07");                          //渠道类型07-PC

        /***商户接入参数***/
        data.put("merId", "898330275230083");                   //商户号码（本商户号码仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        data.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        data.put("orderId", DemoBase.getOrderId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        data.put("txnTime", DemoBase.getCurrentTime());         //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("accType", "01");                              //账号类型

        //卡号，姓名 手机号  必送
        //实名认证交易的customerInfo送什么验证要素一般在入网的时候银联业务会与商户业务商谈好这些内容，请咨询您的业务人员或者银联业务运营的同事，银联端不做配置
        Map<String,String> customerInfoMap = new HashMap<String,String>();
        //customerInfoMap.put("certifTp", "01");						//证件类型
        //customerInfoMap.put("certifId", "341126197709218366");		//证件号码
        customerInfoMap.put("customerNm", param.get("name").toString());					//姓名
        customerInfoMap.put("phoneNo", param.get("username").toString());			    //手机号
        //customerInfoMap.put("pin", "123456");						//密码
        //customerInfoMap.put("cvn2", "123");           			    //卡背面的cvn2三位数字
        //customerInfoMap.put("expired", "1711");  				    //有效期 年在前月在后

        data.put("bindId", param.get("bindid").toString());       //【本交易中bindId必送】可以自行定义 1-128位字母、数字和/或特殊符号字符,代收使用Form09_6_2_DaiShou_BindId.java

        //////////如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo，pin和phoneNo，cvn2，expired加密（如果这些上送的话）对敏感信息加密使用：
        String accNo = AcpService.encryptData(param.get("cardnum").toString(), "UTF-8");  //【贷记卡】这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
        data.put("accNo", accNo);
        data.put("encryptCertId", CertUtil.getEncryptCertId()); 	 //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
        String customerInfoStr = AcpService.getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
        //////////

        /////////如果商户号未开通【商户对敏感信息加密】权限那么不需对敏感信息加密使用：
        //contentData.put("accNo", "6221558812340000");            		 //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
        //String customerInfoStr = DemoBase.getCustomerInfo(customerInfoMap,null);
        ////////

        data.put("customerInfo", customerInfoStr);
        //contentData.put("reqReserved", "透传字段");         				//请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节。出现&={}[]符号时可能导致查询接口应答报文解析失败，建议尽量只传字母数字并使用|分割，或者可以最外层做一次base64编码(base64编码之后出现的等号不会导致解析失败可以不用管)。

        return data;
    }

}
