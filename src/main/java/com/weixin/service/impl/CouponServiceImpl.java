package com.weixin.service.impl;

import com.weixin.dao.CouponMapper;
import com.weixin.pojo.Result;
import com.weixin.service.CouponService;
import com.weixin.util.DateUtils;
import com.weixin.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Administrator on 2018/3/29.
 */
@Service
public class CouponServiceImpl implements CouponService{

    private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    /**
     * 随机分配一张优惠券
     * @return
     */
    @Transactional
    public Result randomAllocationOneCoupon(String token) throws Exception{
        logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券,参数:["+token+"]");
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("startDate","2018-04-11 09:11:34");  //活动开始时间
        param.put("endDate","2018-04-18 23:59:59");     //活动结束时间

        String username = couponMapper.getUsernameByToken(token);
        param.put("username",username);

        //判断是否领取过优惠券
        int isCouponCount = couponMapper.getIsAllocationCoupon(param);
        if(isCouponCount>0){  //已经领取过优惠券
            logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->您已经领取过优惠券");
            return ResultUtil.requestSuccess(null,"您已经领取过优惠券","01");
        }else{
            System.out.println("没有领券");
        }

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
            logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->获取一张优惠券进行分配,参数:["+param+"],返回数据:["+couponMap+"]");
            param.put("coupon_code",couponMap.get("code"));
            param.put("coupon_name",couponMap.get("name"));
            param.put("coupon_amount",couponMap.get("amount"));
            param.put("coupon_owner",couponMap.get("owner"));
            param.put("deadline", DateUtils.formatDateToStr(DateUtils.addOneDay(new Date(),30)));
            //给用户分配一张优惠券
            int count = couponMapper.insertAllocationCoupon(param);
            logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->给用户分配一张优惠券,参数:["+param+"]");
            int cc = couponMapper.insertStatisticsCoupon(param);
            logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->插入一条数据到优惠券统计表中,参数:["+param+"]");
            if(count>0){
                param.put("id",couponMap.get("id"));
                //发放优惠券后,把此优惠券修改为已分配
                if(couponMapper.updateCoupon(param)>0){
                    return ResultUtil.requestSuccess(null);
                }else{
                    logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->修改分配的优惠券为已分配状态,失败!");
                    return ResultUtil.requestFaild(null);
                }
            }else{
                logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->分配优惠券失败");
                return ResultUtil.requestFaild(null);
            }
        }else{
            logger.info("进入2018年 春游活动领券service-->随机分配一张优惠券-->优惠券已发送完,感谢您对一咻的支付");
            return ResultUtil.requestSuccess(null,"优惠券已发送完,感谢您对一咻的支付!","02");
        }
    }

}
