package com.weixin.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.dao.AutologonMapper;
import com.weixin.pojo.Result;
import com.weixin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 * 请求验证token
 */
public class TokenInterceptor implements HandlerInterceptor {


    @Autowired
    private AutologonMapper autologonMapper;

    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        /*Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(httpServletRequest.getParameter("paramStr")));
        if(paramMap.get("js_token")==null||paramMap.get("js_unionid")==null){ //说明是第一次登陆
            String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",paramMap.get("js_code").toString());
            JSONObject json =  RequestWx.doGetWx(url);
            Map<String,Object> tempMap = new HashMap<String,Object>();
            tempMap.put("unionid",json.get("unionid"));
            return true;
        }else{
            if(true){
                return true;
            }else{
                httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestFaild("token不一致")));
                return false;
            }
        }*/
        System.out.println("ontroller 执行之前调用");
        return true;
    }

    /**
     * controller 执行之后，且页面渲染之前调用
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 页面渲染之后调用，一般用于资源清理操作
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
