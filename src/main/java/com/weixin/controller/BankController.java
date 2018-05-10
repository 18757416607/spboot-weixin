package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.service.BankService;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@RestController
@RequestMapping(value = "/bank")
public class BankController {

    private final static Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private BankService bankService;

    /**
     * 根据银行卡号 获取  所属银行名称
     * @param paramStr
     *      {"token":"","cardnum":""}
     * @return
     *      -1：系统报错   00：成功   01：参数为空  02：请输入正确的银行卡号
     */
    @RequestMapping(value = "/getCardByBankName")
    public Result getCardByBankName(String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("根据银行卡号 获取  所属银行名称controller-->参数:["+paramMap+"]");
            if(paramMap.get("cardnum")==null||"".equals(paramMap.get("cardnum"))){
                logger.info("根据银行卡号 获取  所属银行名称controller-->[卡号]参数为空");
                return ResultUtil.requestSuccess("[卡号]参数为空","[卡号]参数为空","01");
            }
            return bankService.getCardByBankName(paramMap);
        }catch (Exception e){
            logger.info("根据银行卡号 获取  所属银行名称controller-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /**
     * 获取 银行活动 列表
     * @param paramStr
     *      {"token":""}
     * @return
     */
    @PostMapping(value = "/getBankActivitList")
    public Result getBankActivitList(String paramStr) {
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("根据获取 银行活动 列表controller-->参数:["+paramMap+"]");
            return bankService.getBankActivitList();
        }catch (Exception e){
            logger.info("根据获取 银行活动 列表controller-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /**
     * 获取阿里大于短信验证
     * @param paramStr
     *      {"token":"",username:18757416607,cardnum:123456789}
     * @return
     *      -1：系统报错   00：成功   01：参数为空  02：阿里大与返回的消息
     */
    @PostMapping(value = "/getAliDaYuCheckCode")
    public Result getAliDaYuCheckCode(String paramStr){
        try {
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("进入获取阿里大于短信验证接口controller-->参数:["+paramMap+"]");
            if(paramMap.get("username")==null||"".equals(paramMap.get("username"))){
                logger.info("获取阿里大于短信验证-->[手机号]为空");
                return ResultUtil.requestSuccess("[手机号]为空","[手机号]为空","01");
            }
            if(paramMap.get("cardnum")==null||"".equals(paramMap.get("cardnum"))){
                logger.info("获取阿里大于短信验证-->[卡号]位空");
                return ResultUtil.requestSuccess("[卡号]为空","[卡号]为空","01");
            }
            Result result = bankService.getAliDaYuCheckCode(paramMap.get("username").toString(),paramMap.get("cardnum").toString());
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


    /**
     * 绑定银行卡
     * @param paramStr
     *      {"token":""}
     * @param P1
     *      {"name":"","checkcode":""}
     *      name : 姓名
     *      checkcode : 验证码
     * @param P2
     *      {"platenum":"","cardnum":"","username":""}
     *      platenum : 车牌号
     *      cardnum : 银行卡号
     *      username : 手机号
     * @return
     *      -1：系统报错   00：成功  01：参数为空  02：表更新不成功   03：验证码失效,请重新获取验证码!  04:验证码错误!   05:银联返回的消息
     * @throws Exception
     */
    @PostMapping(value = "/bindBankCard")
    public Result bindBankCard(String paramStr,String P1,String P2, HttpServletRequest req, HttpServletResponse resp){
        try{
            Map<String,Object> tokenMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            Map<String,Object> p1 = JsonUtil.jsonToMap(SecurityUtils.decrypt(P1));
            Map<String,Object> p2 = JsonUtil.jsonToMap(SecurityUtils.decrypt(P2));

            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("token",tokenMap.get("token"));
            paramMap.put("name",p1.get("name"));
            paramMap.put("checkcode",p1.get("checkcode"));
            paramMap.put("username",p2.get("username"));
            paramMap.put("platenum",p2.get("platenum"));
            paramMap.put("cardnum",p2.get("cardnum"));

            logger.info("进入绑定银行卡controller-->参数:["+paramMap+"]");
            if(paramMap.get("platenum")==null||"".equals(paramMap.get("platenum"))){
                logger.info("绑定银行卡-->[车牌号]为空");
                return ResultUtil.requestSuccess("[车牌号]为空","[车牌号]为空","01");
            }
            if(paramMap.get("cardnum")==null||"".equals(paramMap.get("cardnum"))){
                logger.info("绑定银行卡-->[卡号]为空");
                return ResultUtil.requestSuccess("[卡号]为空","[卡号]为空","01");
            }
            if(paramMap.get("name")==null||"".equals(paramMap.get("name"))){
                logger.info("绑定银行卡-->[姓名]为空");
                return ResultUtil.requestSuccess("[姓名]为空","[姓名]为空","01");
            }
            if(paramMap.get("username")==null||"".equals(paramMap.get("username"))){
                logger.info("绑定银行卡-->[手机号]为空");
                return ResultUtil.requestSuccess("[手机号]为空","[手机号]为空","01");
            }
            if(paramMap.get("checkcode")==null||"".equals(paramMap.get("checkcode"))){
                logger.info("绑定银行卡-->[验证码]为空");
                return ResultUtil.requestSuccess("[验证码]为空","[验证码]为空","01");
            }
            return bankService.bindBankCard(paramMap,req,resp);
        }catch (Exception e){
            logger.info("绑定银行卡controller-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }

    /**
     * 解绑银行卡
     * @param paramStr
     *      {"token":"","platenum":""}
     *      platenum : 车牌号
     * @param req
     * @param resp
     * @return
     *      -1：系统报错   00：成功  01：参数为空  02：表更新不成功 03:车牌号对应的银行卡号不存在
     */
    @PostMapping(value = "/unBindBankCard")
    public Result unBindBankCard(String paramStr, HttpServletRequest req, HttpServletResponse resp){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("进入解绑银行卡controller-->参数:["+paramMap+"]");
            if(paramMap.get("platenum")==null||"".equals(paramMap.get("platenum"))){
                logger.info("解绑银行卡-->[车牌号]为空");
                return ResultUtil.requestSuccess("[车牌号]为空","[车牌号]为空","01");
            }
            if(paramMap.get("cardnum")==null||"".equals(paramMap.get("cardnum"))){
                logger.info("解绑银行卡-->[银行卡号]为空");
                return ResultUtil.requestSuccess("[银行卡号]为空","[银行卡号]为空","01");
            }
            return bankService.UnBindBankCard(paramMap,req,resp);
        }catch (Exception e){
            logger.info("解绑银行卡controller-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


}
