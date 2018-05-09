package com.weixin.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/2/7.
 */
//这里可以使用@Mapper注解，但是每个mapper都加注解比较麻烦，所以统一配置@MapperScan在扫描路径在application类中
@Mapper
@Repository
public interface UserMapper {

    /**
     * 获取  我的优惠券个数
     * @param token
     * @return
     */
    public int getMyCouponCount(String token);

    /**
     * 获取  我的优惠券列表
     * @param token
     * @return
     */
    public List<Map<String,Object>> getMyCouponList(String token);

}
