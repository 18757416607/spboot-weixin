package com.weixin.util;

import java.util.UUID;

/**
 * Created by Administrator on 2018/3/28.
 * UUID 工具类
 */
public class UuidUtils {

    /**
     * 获取32位UUID
     * @return
     */
    public static String get32UUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
