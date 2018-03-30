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
     * 更新unionid和token
     * @param param
     * @return
     */
    public int updateWechatUser(Map<String,Object> param);


}
