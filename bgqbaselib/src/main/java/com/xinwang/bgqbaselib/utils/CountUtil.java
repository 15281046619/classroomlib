package com.xinwang.bgqbaselib.utils;

import java.math.BigDecimal;

/**
 * double 乘法不准确 精度丢失
 * Date:2021/4/13
 * Time;9:29
 * author:baiguiqiang
 */
public class CountUtil {
    public static  Double multiply(int mInt,Double dmDouble){
        BigDecimal a = new BigDecimal(String.valueOf(mInt));
        BigDecimal b = new BigDecimal(String.valueOf(dmDouble));
        return a.multiply(b).doubleValue();
    }
    public static Double  add(Double a, Double b){
        BigDecimal c = new BigDecimal(String.valueOf(a));
        BigDecimal d = new BigDecimal(String.valueOf(b));
        return  c.add(d).doubleValue();
    }
    public static Double  sub(Double a, Double b){
        BigDecimal c = new BigDecimal(String.valueOf(a));
        BigDecimal d = new BigDecimal(String.valueOf(b));
        return  c.subtract(d).doubleValue();
    }
}
