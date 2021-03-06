package com.weixin.util.acp.demo.jlwtDaishou;

import com.weixin.util.acp.demo.DemoBase;
import com.weixin.util.acp.sdk.AcpService;
import com.weixin.util.acp.sdk.LogUtil;
import com.weixin.util.acp.sdk.SDKConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用途：建立委托（后台交易）<br>
 * 日期： 2017-07<br>
 * 声明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障<br>
 */

public class Form09_6_6_Jlwt_Back extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		/**
		 * 请求银联接入地址，获取证书文件，证书路径等相关参数初始化到SDKConfig类中
		 * 在java main 方式运行时必须每次都执行加载
		 * 如果是在web应用开发里,这个方法可使用监听的方式写入缓存,无须在这出现
		 */
		//这里已经将加载属性文件的方法挪到了web/AutoLoadServlet.java中
		//SDKConfig.getConfig().loadPropertiesFromSrc(); //从classpath加载acp_sdk.properties文件
		super.init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String merId = req.getParameter("merId");
		String orderId = req.getParameter("orderId");
		String txnTime = req.getParameter("txnTime");
		
		String accNo = req.getParameter("accNo");
		String certifTp = req.getParameter("certifTp");
		String certifId = req.getParameter("certifId");
		String customerNm = req.getParameter("customerNm");
		String phoneNo = req.getParameter("phoneNo");
		String cvn2 = req.getParameter("cvn2");
		String expired = req.getParameter("expired");
		
		Map<String, String> contentData = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		contentData.put("version", DemoBase.version);                  //版本号
		contentData.put("encoding", DemoBase.encoding);                //字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("signMethod", 
				SDKConfig.getConfig().getSignMethod());                //签名方法 目前只支持01-RSA方式证书加密
		contentData.put("txnType", "72");                              //交易类型
		contentData.put("txnSubType", "11");                           //交易子类型
		contentData.put("bizType", "000501");                          //业务类型
		contentData.put("channelType", "07");                          //渠道类型

		/***商户接入参数***/
		contentData.put("merId", merId);                  			   //商户号码（商户号码777290058110097仅做为测试调通交易使用，该商户号配置了需要对敏感信息加密）测试时请改成自己申请的商户号，【自己注册的测试777开头的商户号不支持代收产品】
		contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改	
		contentData.put("orderId", orderId);             			   //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则	
		contentData.put("txnTime", txnTime);         				   //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("accType", "01");                              //账号类型
		
		//姓名，证件类型+证件号码至少二选一必送，手机号可选，贷记卡的cvn2,expired可选。
		Map<String,String> customerInfoMap = new HashMap<String,String>();
		customerInfoMap.put("certifTp", certifTp);						//证件类型
		customerInfoMap.put("certifId", certifId);						//证件号码
		customerInfoMap.put("customerNm", customerNm);					//姓名
		
		customerInfoMap.put("phoneNo", phoneNo);                        //手机号
		//当卡号为贷记卡的时候cvn2,expired可选上送
		customerInfoMap.put("cvn2", cvn2);           					//卡背面的cvn2三位数字
		customerInfoMap.put("expired", expired);  				    	//有效期 年在前月在后
		
		//如果商户号开通了【商户对敏感信息加密】的权限那么需要对
		//accNo，pin和phoneNo，cvn2，expired加密（如果这些上送的话），对敏感信息加密使用：
		String accNoEnc = AcpService.encryptData(accNo, "UTF-8"); 		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		contentData.put("accNo", accNoEnc);
		contentData.put("encryptCertId",AcpService.getEncryptCertId()); //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
		String customerInfoStr = AcpService.
				getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
		
		
		//如果商户号未开通【商户对敏感信息加密】权限那么不需对敏感信息加密使用：
		//contentData.put("accNo", "6216261000000000018");            		//这里测试的时候使用的是测试卡号，正式环境请使用真实卡号
		//String customerInfoStr = DemoBase.getCustomerInfo(customerInfoMap,null);
		
		contentData.put("customerInfo", customerInfoStr);

		/**对请求参数进行签名并发送http post请求，接收同步应答报文**/
		Map<String, String> reqData = AcpService.sign(contentData,DemoBase.encoding);			//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestBackUrl = SDKConfig.getConfig().getBackRequestUrl();   			//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(reqData,requestBackUrl,DemoBase.encoding); //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, DemoBase.encoding)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode") ;
				if(("00").equals(respCode)){
					//成功
					//TODO
				}else{
					//其他应答码为失败请排查原因或做失败处理
					//TODO
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				//TODO 检查验证签名失败的原因
			}
		}else{
			//未返回正确的http状态
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
		}
		String reqMessage = DemoBase.genHtmlResult(reqData);
		String rspMessage = DemoBase.genHtmlResult(rspData);
		resp.getWriter().write("请求报文:<br/>"+reqMessage+"<br/>" + "应答报文:</br>"+rspMessage+"");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	

}
