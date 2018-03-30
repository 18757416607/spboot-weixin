package com.weixin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 * 优惠卷
 */
@Repository
@Mapper
public interface CouponMapper {

    /**
     * java代码随机获取一个owner,根据owner获取一张没分配的优惠券
     * @param param
     * @return
     */
    public Map<String,Object> findRandomAllocationOneCoupon(Map<String,Object> param);

    /**
     * 分配优惠券后,修改优惠券信息
     * @param param
     * @return
     */
    public int updateCoupon(Map<String,Object> param);

    /**
     * 活动开始时间和活动结束时间  获取已经分配的优惠券数量
     * @param param
     * @return
     */
    public List<Map<String,Object>> getAllocationCountByOwnerAndDate(Map<String,Object> param);

    /**
     * 添加分配的优惠卷
     * @param param
     * @return
     */
    public int insertAllocationCoupon(Map<String,Object> param);
}
