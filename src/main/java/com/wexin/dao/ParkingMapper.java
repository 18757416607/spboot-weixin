package com.wexin.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
@Repository
@Mapper
public interface ParkingMapper{

    /**
     * 验证数据是否有效
     * @param param
     * @return
     */
    public int checkDataIsValid(Map<String,String> param);


    /**
     * 批量添加未合作停车场信息
     * @param paramList
     * @return
     */
    public int addBatchNonCooperationPark(List<Map<String,Object>> paramList);

    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * @param param
     * @return
     */
    public List<Map<String,String>> findNearbyPark(Map<String,String> param);

}
