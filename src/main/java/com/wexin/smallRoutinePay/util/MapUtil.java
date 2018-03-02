package com.wexin.smallRoutinePay.util;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/3/1.
 * Map工具转换类
 */
public class MapUtil {

    /**
     * 加密验签使用  Map转换String
     * @param param
     * @return
     */
    public static String MapToString(TreeMap<String,String> param){
        Iterator<Map.Entry<String, String>> it= param.entrySet().iterator();
        StringBuffer str = new StringBuffer();
        while(it.hasNext()){
            Map.Entry<String, String> entry=it.next();
            str.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return  str.substring(0,str.length()-1);
    }

}
