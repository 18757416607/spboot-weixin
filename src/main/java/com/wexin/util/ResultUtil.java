package com.wexin.util;

import com.wexin.pojo.Result;

/**
 * Created by Administrator on 2018/3/5.
 */
public class ResultUtil {

    public static String REQUESTFAILD = "请求失败";


    /**
     * 请求成功
     * @param data
     * @return
     */
    public static Result requestSuccess(Object data){
        Result result = new Result();
        result.setCode("00");
        result.setMsg("请求成功");
        result.setData(data);
        return  result;
    }

    /**
     * 请求失败
     * @return
     */
    public static  Result requestFaild(String msg){
        Result result = new Result();
        result.setCode("-1");
        if(msg==null){
            result.setMsg(REQUESTFAILD);
        }else{
            result.setMsg(msg);
        }
        return  result;
    }

}
