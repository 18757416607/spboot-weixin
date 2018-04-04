package com.weixin.service;

import com.sun.org.apache.regexp.internal.RE;
import com.weixin.pojo.Result;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 */
public interface AutologonService {

    /**
     * 验证tonken是否存在
     * @param token
     * @return
     */
    public int getTokenCount(String token);

    /**
     * 验证unionid是否存在  手机号是否绑定了unionid
     * @param unionid
     * @return
     */
    public int getUnionidCount(String unionid);

    /**
     * 根据unionid获取token
     * @param unionid
     * @return
     */
    public String getTokenByUnionid(String unionid);

    /**
     * 绑定手机号
     * @param param
     * @return
     */
    public Result bindPhone(Map<String,Object> param) throws Exception;

    /**
     * 获取阿里大于短信验证
     * @return
     */
    public Result getAliDaYuCheckCode(String phone) throws  Exception;

    /**
     * 微信小程序授权登陆
     * @param phone
     * @param code
     * @return
     */
    public Result authorizationLogin(Map<String,Object> param) throws Exception;


}
