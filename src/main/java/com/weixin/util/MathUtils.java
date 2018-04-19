package com.weixin.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/4/18.
 * 计算类
 */
public class MathUtils {

    /**
     * 一个值 在一组值里判断和哪个最接近
     * @param x
     * @param src
     * @return
     */
    public static double getApproximate(double x, double[] src) {
        if (src == null) {
            return -1;
        }
        if (src.length == 1) {
            return src[0];
        }
        double minDifference = Math.abs(src[0] - x);
        int minIndex = 0;
        for (int i = 1; i < src.length; i++) {
            double temp = Math.abs(src[i] - x);
            if (temp < minDifference) {
                minIndex = i;
                minDifference = temp;
            }
        }
        return src[minIndex];
    }


}
