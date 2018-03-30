package com.weixin.service;

import com.weixin.pojo.Result;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 */
public interface AutologonService {

    /**
     * 微信自动登陆
     * @param param
     * @return
     */
    public String wxLogin(Map<String,Object> param) throws Exception;


}
