package com.weixin.controller;

import com.weixin.pojo.Result;
import com.weixin.service.AutologonService;
import com.weixin.util.JsonUtil;
import com.weixin.util.ResultUtil;
import com.weixin.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 * 自动登陆
 */
@RestController
@RequestMapping(value = "/autologon")
public class AutologonController {

    @Autowired
    private AutologonService autologonService;

    /**
     * 绑定手机号
     * @param paramStr
     * @return
     */
    @PostMapping(value = "/bindPhone", produces = {"application/json;charset=UTF-8;"})
    public Result bindPhone(@RequestBody String paramStr) throws Exception {
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            if(paramMap.get("phone")==null||"".equals(paramMap.get("phone"))){
                return ResultUtil.requestFaild("手机号不能为空!");
            }
            String token = autologonService.bindPhone(paramMap);
            return ResultUtil.requestSuccess(token);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }

    }

    /**
     * 获取阿里大于短信验证
     * @return
     */
    @PostMapping(value = "/getAliDaYuCheckCode", produces = {"application/json;charset=UTF-8;"})
    public Result getAliDaYuCheckCode(@RequestBody String paramStr){
        try {
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(paramStr));
            if(paramMap.get("phone")==null||"".equals(paramMap.get("phone"))){
                return ResultUtil.requestFaild("手机号不能为空!");
            }
            return autologonService.getAliDaYuCheckCode(paramMap.get("phone").toString());
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(e.getMessage());
        }
    }


}
