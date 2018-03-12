package com.weixin.smallRoutinePay.controller;

import com.weixin.pojo.Result;
import com.weixin.smallRoutinePay.service.AppPayService;
import com.weixin.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2018/3/1.
 * 小程序支付
 */
@Controller
@RequestMapping(value = "/appPay")
public class AppPayController {

    @Autowired
    private AppPayService appPayService;

    /**
     * 停车费用
     * @throws Exception
     */
    @PostMapping(value = "/parkStopCost")
    public Result parkStopCost(@RequestBody String paramStr) throws  Exception{
        Map paramMap = JsonUtil.jsonToMap(/*SecurityUtils.decrypt(*/paramStr/*)*/);
        return appPayService.parkStopCost(paramMap);
    }

    @PostMapping(value = "/appPayNotifyUrl")
    public void appPayNotifyUrl(){
        System.out.println("小程序支付回调成功");
   }


}
