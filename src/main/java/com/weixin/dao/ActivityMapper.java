package com.weixin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2018/3/29.
 */
@Repository
@Mapper
public interface ActivityMapper {

    /**
     * 添加一个活动信息
     * @param activityId
     * @param activityName
     * @return
     */
    public int insertActivity(@Param(value = "activityId") String activityId, @Param(value = "activityName") String activityName, @Param(value = "openid") String openid);

}
