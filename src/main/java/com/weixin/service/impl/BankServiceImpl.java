package com.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.weixin.dao.BankMapper;
import com.weixin.pojo.Result;
import com.weixin.service.BankService;
import com.weixin.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@Service
public class BankServiceImpl implements BankService{

    private final static Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);

    @Autowired
    private BankMapper bankMapper;

    /**
     * 根据银行卡号 获取  所属银行名称
     * @param param
     *         {"token":"","card":""}
     *         card:银行卡号
     * @return
     */
    public Result getCardByBankName(Map<String,Object> param) throws Exception{
        logger.info("进入根据银行卡号 获取  所属银行名称 service");
        String cardbin = param.get("card").toString().substring(0,6);
        logger.info("根据银行卡号 获取  所属银行名称 -->前六位卡bin:"+cardbin);
        String bankName = bankMapper.getCardByBankName(cardbin);
        logger.info("根据银行卡号 获取  所属银行名称 -->获取的银行名称:"+bankName);
        return ResultUtil.requestSuccess(bankName);
    }


    /**
     * 获取 银行活动 列表
     * @return
     */
    public Result getBankActivitList() throws Exception{
        logger.info("进入获取 银行活动 列表 service");
        List<Map<String,Object>> bankActivitList = bankMapper.getBankActivitList();
        logger.info("根据获取 银行活动 列表 --> 数据:"+bankActivitList);
        return ResultUtil.requestSuccess(JSON.toJSON(bankActivitList).toString());
    }


}
