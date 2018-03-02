package com.wexin.dao;

import com.wexin.pojo.Parking;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
@Repository
@Mapper
public interface ParkingMapper{

    public int checkDataIsValid(Map<String,String> param);

}
