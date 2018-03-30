package com.weixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.weixin.pojo.Result;
import com.weixin.service.AutologonService;
import com.weixin.util.Constant;
import com.weixin.util.RequestWx;
import com.weixin.util.ResultUtil;
import com.weixin.util.UuidUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/28.
 */
@Service
public class AutologonServiceImpl implements AutologonService{

    /**
     * 微信自动登陆
     * @param param
     * @return
     */
    public String wxLogin(Map<String,Object> param) throws Exception{
        if(param.get("js_token")!=null&&!"".equals(param.get("js_token"))){

        }else{
            String token = UuidUtils.get32UUID();
        }
        String url = Constant.XCX_CODE_URL.replace("APPID",Constant.XCX_APPID).replace("SECRET",Constant.XCX_APPSECRET).replace("JSCODE",param.get("js_code").toString());
        JSONObject json =  RequestWx.doGetWx(url);
        return JSONObject.toJSONString(json);
    }


}
