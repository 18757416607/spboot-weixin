package com.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.pojo.Result;
import com.weixin.service.AutologonService;
import com.weixin.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 * 自动登陆
 */
@RestController
@RequestMapping(value = "/autologon")
public class AutologonController {

    private final Logger logger = LoggerFactory.getLogger(AutologonController.class);

    @Autowired
    private AutologonService autologonService;

    /**
     * 前端没有token时,需要给前端返回tonken
     * @param paramStr
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/getToken"/*, produces = {"application/json;charset=UTF-8;"}*/)
    private void isOpenLoginView(/*@RequestBody*/ String paramStr) throws Exception{
        System.out.println(paramStr);

        /*try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            //调用微信官网接口获取unionid
            String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",paramMap.get("code").toString());
            JSONObject json =  RequestWx.doGetWx(url);
            String token = autologonService.getTokenByUnionid(json.get("unionid").toString());
            return ResultUtil.requestSuccess(token);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }*/
    }


    /**
     * 绑定手机号
     * @param paramStr
     * @return
     */
    @PostMapping(value = "/bindPhone", produces = {"application/json;charset=UTF-8;"})
    public Result bindPhone(@RequestBody String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            if(paramMap.get("phone")==null||"".equals(paramMap.get("phone"))){
                logger.info("自动登陆controller-->绑定手机号-->手机号不能为空:["+paramMap+"]");
                return ResultUtil.requestFaild("手机号不能为空!");
            }
            if(paramMap.get("code")==null||"".equals(paramMap.get("code"))){
                logger.info("自动登陆controller-->绑定手机号-->code不能为空:["+paramMap+"]");
                return ResultUtil.requestFaild("code不能为空!");
            }
            return autologonService.bindPhone(paramMap);
        }catch (Exception e){
            logger.info("自动登陆controller-->绑定手机号-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }

    }

    /**
     * 获取阿里大于短信验证
     * @return
     */
    @PostMapping(value = "/getAliDaYuCheckCode", produces = {"application/json;charset=UTF-8;"})
    public Result getAliDaYuCheckCode(@RequestBody String paramStr){
        try {
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("自动登陆controller-->获取阿里大于短信验证-->参数:["+paramMap+"]");
            if(paramMap.get("phone")==null||"".equals(paramMap.get("phone"))){
                logger.info("自动登陆controller-->获取阿里大于短信验证-->手机号不能为空");
                return ResultUtil.requestFaild("[phone]参数为空!");
            }
            return autologonService.getAliDaYuCheckCode(paramMap.get("phone").toString());
        }catch (Exception e){
            logger.info("自动登陆controller-->获取阿里大于短信验证-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }



    /**
     * 微信小程序授权登陆
     * @param paramStr
     * @throws Exception
     */
    @PostMapping(value = "/authorizationLogin")
    public Result authorizationLogin(String paramStr) throws Exception{
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("自动登陆controller-->微信小程序授权登陆-->参数:["+paramMap+"]");
            if(paramMap.get("phone")==null||"".equals(paramMap.get("phone"))){
                logger.info("自动登陆controller-->微信小程序授权登陆-->[phone]参数为空");
                return ResultUtil.requestFaild("[phone]参数为空");
            }
            if(paramMap.get("code")==null||"".equals(paramMap.get("code"))){
                logger.info("自动登陆controller-->微信小程序授权登陆-->[code]参数为空");
               return ResultUtil.requestFaild("[code]参数为空");
            }
            return autologonService.authorizationLogin(paramMap);
        }catch (Exception e){
            logger.info("自动登陆controller-->微信小程序授权登陆-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }





   public static void main(String[] args)  throws Exception{
        //decrypt(String data, String key, String iv, String encodingFormat) throws Exception {
        /*String s = AesCbcUtil.decrypt("HcsN1q531hKdTLAxDTwOl8j/9kT0V7J9xOS5KUWindLJ+ooAsv…GzVKUQ37kNH7RcU9k3nJv8NLNjzlKGiMhF+U4kzVbgareEQ==","/Pr9Y+wtm4HS5wqiZ/hTaw==","OxKV6zce6ieF05ItImm14Q==","UTF-8");
       System.out.println(s);*/
        //{"unionid":"oPUXTtyNyCHkXA0iV_IvE6T-NyIw","openid":"oMWvv0Hf5PfPSyNYqvgKFsbdJX5Q","session_key":"/Pr9Y+wtm4HS5wqiZ/hTaw==","expires_in":7200}
       String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET)
               .replace("JSCODE","011SlJnL1KCbv418RXoL1p60oL1SlJnp");
       JSONObject json =  RequestWx.doGetWx(url);
       System.out.println(json);

      /*  String str = "PnsAIhfq55Wfyrsfk7mXbylACF62FKL/znrrBtZexvTASnAiax…anmFmIAX9NsufPJafsA/v0+eaF+HdHh9ugk1D0RktViq9Kw==";
        byte[] dataByte = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(str);
        // 加密秘钥
        byte[] keyByte = org.apache.tomcat.util.codec.binary.Base64.decodeBase64("ijXNUmCdzSSXxaldh51XhA==");
        // 偏移量
        byte[] ivData = org.apache.tomcat.util.codec.binary.Base64.decodeBase64("Hn73dR6DB6GLND7RY7uqCA==");
        try {

            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            //AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivData);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivData));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                System.out.println(JSON.parseObject(result));
            }
        } catch (Exception e){
            e.printStackTrace();
        }*/
    }


}