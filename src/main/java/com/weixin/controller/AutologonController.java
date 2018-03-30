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
     * 自动登陆
     * @param request
     * @return
     */
    @PostMapping(value = "/wxLogin")
    public Result wxLogin(HttpServletRequest request){
        try{
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(request.getParameter("paramStr")));
            String jsonStr = autologonService.wxLogin(paramMap);
            return ResultUtil.requestSuccess(jsonStr);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.requestFaild(ResultUtil.REQUESTFAILD);
        }
    }



}
