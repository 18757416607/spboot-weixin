package com.weixin.util;

/**
 * Created by Administrator on 2018/4/2.
 * 随机数工具
 */
public class RandomUtils {


    /**
     * 生成6位随机数
     * @return
     */
    public static String Generate6RandomNumbers(){
        Double random = Math.random();
        String strRandom = random.toString();
        return strRandom.substring(strRandom.length()-6,strRandom.length());
    }

}
