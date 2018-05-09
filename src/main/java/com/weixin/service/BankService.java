package com.weixin.service;

import com.weixin.pojo.Result;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
public interface BankService {

    /**
     * 根据银行卡号 获取  所属银行名称
     * @param param
     *      {"token":"","card":""}
     *      card:银行卡号
     * @return
     */
    public Result getCardByBankName(Map<String,Object> param) throws Exception;

    /**
     * 获取 银行活动 列表
     * @return
     */
    public Result getBankActivitList() throws Exception;

}
