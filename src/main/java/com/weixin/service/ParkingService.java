package com.weixin.service;

import com.weixin.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/2.
 */
public interface ParkingService {

    /**
     * 验证数据是否有效
     * @param param
     * @return
     */
    public int checkDataIsValid(Map<String,String> param);

    /**
     * 批量添加未合作停车场信息
     * @return
     * @throws Exception
     */
    public Result addBatchNonCooperationPark() throws Exception;

    /**
     * 批量添加未合作停车场信息
     * @return
     * @throws Exception
     */
    public Result addBatchNonCooperationPark_excel(MultipartFile excelFile) throws Exception;


    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * @param param
     * @return
     * @throws Exception
     */
    public Result findNearbyPark(Map<String,String> param) throws  Exception;

    /**
     * 利用经度(longitude)(121)纬度(latitude)(29)查询附近停车场  00：未合作停车场  01：合作停车场
     * @param param
     * @return
     * @throws Exception
     */
    public Result findNearbyParkNew(Map<String,String> param) throws  Exception;

    /**
     * 获取所有的合作停车场列表
     * @return
     */
    public Result findPrakList();

}
