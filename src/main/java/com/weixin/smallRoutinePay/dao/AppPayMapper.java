package com.weixin.smallRoutinePay.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/12.
 */
@Repository
@Mapper
public interface AppPayMapper {

    /**
     * 查看优惠卷是否存在
     * @param param
     * @return
     */
    public Integer findCouponIsExist(Map<String,String> param);

}
