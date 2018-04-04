package com.weixin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */
@Repository
@Mapper
public interface AutologonMapper {

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
     * 根据unionid获取username(phone)
     * @param unionid
     * @return
     */
    public String getUserNameByUnionid(String unionid);

    /**
     * 根据手机号获取绑定消息  绑定手机号时验证手机号是否相同
     * @param phone
     * @return
     */
    public Map<String,Object> getBindInfoByPhone(String phone);

    /**
     * 更新WechatUser表记录
     * @param param
     * @return
     */
    public int updateWechatUser(Map<String,Object> param);

    /**
     * 插入一条绑定记录
     * @param param
     * @return
     */
    public int insertWechatUser(Map<String,Object> param);

}
