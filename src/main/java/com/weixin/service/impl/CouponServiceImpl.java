package com.weixin.service.impl;

import com.weixin.dao.CouponMapper;
import com.weixin.pojo.Result;
import com.weixin.service.CouponService;
import com.weixin.util.DateUtils;
import com.weixin.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */
@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 随机分配一张优惠券
     * @param param
     * @return
     */
    @Transactional
    public Result randomAllocationOneCoupon(Map<String,Object> param) throws Exception{

        //检测是否领取

        param.put("startDate","2018-02-03 13:11:34");  //活动开始时间
        param.put("endDate","2018-02-9 13:11:34");     //活动结束时间
        //活动开始时间和活动结束时间  获取已经分配的优惠券数量
        List<Map<String,Object>> ownerList = couponMapper.getAllocationCountByOwnerAndDate(param);
        Map<String,Object> money_1 = ownerList.get(0);  //一元优惠券已分配的数量
        Map<String,Object> money_2 = ownerList.get(1);  //二元优惠券已分配的数量
        Map<String,Object> money_3 = ownerList.get(2);  //三元优惠券已分配的数量
        Map<String,Object> money_5 = ownerList.get(3);  //五元优惠券已分配的数量

        //随机获取一种优惠券的种类
        List<Integer> ownerTypeList = new ArrayList<Integer>() ; //春游的优惠券种类
        ownerTypeList.add(845);
        ownerTypeList.add(847);
        ownerTypeList.add(865);
        ownerTypeList.add(858);
        int owner = -1;
        //循环4次,优惠券种类有4种
        for(int i = 0;i<4;i++){
            int index = (int) (Math.random() * ownerTypeList.size());
            owner = ownerTypeList.get(index);
            if(owner==845){ //一元优惠券
                if(Integer.parseInt(money_1.get("num").toString())>=4000){
                    ownerTypeList.remove(0);
                }else{
                    break;
                }
            }else if(owner==847){ //二元优惠券
                if(Integer.parseInt(money_2.get("num").toString())>=1500){
                    ownerTypeList.remove(1);
                }else{
                    break;
                }
            }else if(owner==865){ //三元优惠券
                if(Integer.parseInt(money_3.get("num").toString())>=400){
                    ownerTypeList.remove(2);
                }else{
                    break;
                }
            }else if(owner==858){ //五元优惠券
                if(Integer.parseInt(money_5.get("num").toString())>=100){
                    ownerTypeList.remove(3);
                }else{
                    break;
                }
            }
        }

        if(owner!=-1){
            //查询一张优惠券
            param.put("owner",owner);
            Map<String,Object> couponMap = couponMapper.findRandomAllocationOneCoupon(param);
            param.put("coupon_code",couponMap.get("code"));
            param.put("coupon_name",couponMap.get("name"));
            param.put("coupon_amount",couponMap.get("amount"));
            param.put("coupon_owner",couponMap.get("owner"));
            param.put("deadline", DateUtils.formatDateToStr(DateUtils.addOneDay(new Date(),30)));
            // //给用户分配一张优惠券
            int count = couponMapper.insertAllocationCoupon(param);
            if(count>0){
                param.put("id",couponMap.get("id"));
                //发放优惠券后,把此优惠券修改为已分配
                if(couponMapper.updateCoupon(param)>0){
                    return ResultUtil.requestSuccess(null);
                }else{
                    return ResultUtil.requestFaild(null);
                }
            }else{
                return ResultUtil.requestFaild(null);
            }
        }else{
            return ResultUtil.requestSuccess(null,"优惠券已发送完,感谢您对一咻的支付!");
        }
    }

}
