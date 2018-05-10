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
public class UnBindCardUtil {

    /**
     * 请求银联绑定卡片接口
     * @param req
     * @param resp
     * @param cardNum  银行卡号
     * @return
     * @throws Exception
     */
    public Result requestUnBindCard(HttpServletRequest req, HttpServletResponse resp,String cardNum) throws Exception{
        SDKConfig.getConfig().loadPropertiesFromSrc(); //从classpath加载acp_sdk.properties文件
        /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData = AcpService.sign(setFormDate(cardNum),DemoBase.encoding);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();   			//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData,requestBackUrl,DemoBase.encoding); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

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
    private static Map<String, String> setFormDate(String cardNum) {

        Map<String, String> contentData = new HashMap<String, String>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        contentData.put("version", DemoBase.version);                  //版本号
        contentData.put("encoding", DemoBase.encoding);                //字符集编码 可以使用UTF-8,GBK两种方式
        contentData.put("signMethod", SDKConfig.getConfig().getSignMethod());                           //签名方法 目前只支持01-RSA方式证书加密
        contentData.put("txnType", "74");                              //交易类型 11-代收
        contentData.put("txnSubType", "04");                           //交易子类型  01-实名认证
        contentData.put("bizType", "000501");                          //业务类型 代收产品
        contentData.put("channelType", "07");                          //渠道类型07-PC

        /***商户接入参数***/
        contentData.put("merId", "898330275230083");                   //商户号码（商户号码777290058110097仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
        contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
        contentData.put("orderId", DemoBase.getOrderId());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        contentData.put("txnTime", DemoBase.getCurrentTime());         //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

        //如果商户号开通了【商户对敏感信息加密】的权限那么需要对 accNo，pin和phoneNo，
        //cvn2，expired加密（如果这些上送的话），对敏感信息加密使用：
        String accNoEnc = AcpService.encryptData(cardNum, "UTF-8");  	   //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
        contentData.put("accNo", accNoEnc);
        contentData.put("encryptCertId",AcpService.getEncryptCertId());//加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下

        //如果商户号未开通【商户对敏感信息加密】权限那么不需对敏感信息加密使用：
        contentData.put("accNo",cardNum);            				   //这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
        return contentData;
    }

}
