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
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }


    public static void main(String[] args) {
        System.out.println(get32UUID().length());
    }

}
