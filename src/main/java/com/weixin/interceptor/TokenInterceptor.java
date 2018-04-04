package com.weixin.interceptor;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weixin.pojo.Result;
import com.weixin.service.AutologonService;
import com.weixin.util.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
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
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws IOException {
        System.out.println("进入拦截器-----------------------------");
        try{
            /*-------------------------------------获取json格式数据-------------------------------------*/
            /*StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader reader = httpServletRequest.getReader();
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }*/
            /*-------------------------------------获取json格式数据-------------------------------------*/

             /*-------------------------------------解决service为null无法注入问题-------------------------------------*/
            if (autologonService == null) {//
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(httpServletRequest.getServletContext());
                autologonService = (AutologonService) factory.getBean("autologonServiceImpl");
            }
             /*-------------------------------------解决service为null无法注入问题-------------------------------------*/

            //Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(sb.toString()));
            Map paramMap = JsonUtil.jsonToMap(SecurityUtils.decrypt(httpServletRequest.getParameter("paramStr")));
            if(paramMap.get("token")==null||"".equals(paramMap.get("token"))){  //前端没有token缓存
                if(paramMap.get("code")==null||"".equals(paramMap.get("code"))){
                    httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestSuccess(null,"code不能为空","01")));
                    return false;
                }else{
                    //调用微信官网接口获取unionid
                    String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",paramMap.get("code").toString());
                    JSONObject json =  RequestWx.doGetWx(url);
                    String searchTonken = autologonService.getTokenByUnionid(json.get("unionid").toString());
                    System.out.println("获取的tonken："+searchTonken);
                    if(searchTonken!=null&&!"".equals(searchTonken)){  //说明unionid存在,绑定过手机号
                        if(httpServletRequest.getServletPath().equals("/autologon/getToken")){//获取tonken的接口直接在拦截器中处理了
                            httpServletResponse.getWriter().print(new ObjectMapper().writeValueAsString(ResultUtil.requestSuccess(searchTonken)));
                            return false;
                        }
                        //String searchToken = autologonService.getTokenByUnionid(json.get("unionid").toString());
                        return true;
                    }else{
                        httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestSuccess(null,"您还没绑定过手机,需要先绑定!","01")));
                        return false;
                    }
                }
            }else{ //前端有token缓存
                int tokenCount = autologonService.getTokenCount(paramMap.get("token").toString());
                if(tokenCount>0){
                    System.out.println("有token");
                    return true;
                }else{
                    httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestSuccess(null,"非法token","01")));
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            httpServletResponse.getWriter().print(JSON.toJSON(ResultUtil.requestFaild(e.getMessage())));
            return false;
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
