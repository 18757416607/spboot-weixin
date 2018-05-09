package com.weixin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9.
 */
@Repository
@Mapper
public interface BankMapper {

    /**
     * 根据银行卡号前六位 获取  所属银行名称
     * @param cardbin 行卡号前六位
     * @return
     */
    public String getCardByBankName(String cardbin);

    /**
     * 获取 银行活动 列表
     * @return
     */
    public List<Map<String,Object>> getBankActivitList();

}
