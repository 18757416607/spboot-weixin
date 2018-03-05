package com.wexin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface ParkingService {

    public int checkDataIsValid(Map<String,String> param);

    public int addBatchNonCooperationPark();

    public Map<String,Object> findNearbyPark(Map<String,String> param);

}
