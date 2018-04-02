package com.weixin.interceptor;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.weixin.service.AutologonService;
import com.weixin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 * 请求验证token
 */
public class TokenInterceptor implements HandlerInterceptor {


    @Autowired
    private AutologonService autologonService;

    /**
     * controller 执行之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(httpServletRequest.getParameter("paramStr")));
        if(paramMap.get("token")==null||"".equals(paramMap.get("token"))){  //前端没有token缓存
            if(paramMap.get("code")==null||"".equals(paramMap.get("code"))){
                httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestFaild("code不能为空")));
                return false;
            }else{
                //调用微信官网接口获取unionid
                String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",paramMap.get("code").toString());
                JSONObject json =  RequestWx.doGetWx(url);
                int unionidCount = autologonService.getUnionidCount(json.get("unionid").toString());
                if(unionidCount>0){  //说明unionid存在,绑定过手机号
                    //String searchToken = autologonService.getTokenByUnionid(json.get("unionid").toString());
                    return true;
                }else{
                    httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestFaild("您还没绑定过手机,需要先绑定!")));
                    return false;
                }
            }
        }else{ //前端有token缓存
            int tokenCount = autologonService.getTokenCount(paramMap.get("token").toString());
            if(tokenCount>0){
                return true;
            }else{
                httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestFaild("非法token")));
                return false;
            }
        }
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
