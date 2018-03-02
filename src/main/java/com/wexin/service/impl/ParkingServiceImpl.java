package com.wexin.service.impl;

import com.wexin.dao.ParkingMapper;
import com.wexin.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
@Service
public class ParkingServiceImpl implements ParkingService{

    @Autowired
    private ParkingMapper parkingMapper;

    public int checkDataIsValid(Map<String,String> param){
        return parkingMapper.checkDataIsValid(param);
    }

}
