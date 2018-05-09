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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     *      {"token":"","card":""}
     * @return
     */
    @RequestMapping(value = "/getCardByBankName")
    public Result getCardByBankName(String paramStr){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            logger.info("根据银行卡号 获取  所属银行名称controller-->参数:["+paramMap+"]");
            if(paramMap.get("card")==null||"".equals(paramMap.get("card"))){
                logger.info("根据银行卡号 获取  所属银行名称controller-->[卡号]参数为空");
                return ResultUtil.requestFaild("[卡号]参数为空");
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
            return bankService.getCardByBankName(paramMap);
        }catch (Exception e){
            logger.info("根据获取 银行活动 列表controller-->"+e.getMessage());
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }



}
