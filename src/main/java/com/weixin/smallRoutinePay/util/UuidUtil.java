package com.weixin.smallRoutinePay.util;

import java.util.UUID;

/**
 * Created by Administrator on 2018/3/7.
 */
public class UuidUtil {

    /**
     * 生成32为UUID
     * @return
     */
    public static String get32UUID(){
        return UUID.randomUUID().toString().replace("-","");
    }


    public static void main(String[] args) {
        System.out.println(get32UUID().length());
    }

}
