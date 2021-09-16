package com.xinwang.bgqbaselib.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

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
    public static  Integer multiplyByInt(int mInt,Float dmDouble){
        BigDecimal a = new BigDecimal(String.valueOf(mInt));
        BigDecimal b = new BigDecimal(String.valueOf(dmDouble));
        return a.multiply(b).intValue();
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
    public static Float  sub(Float a, Float b){
        BigDecimal c = new BigDecimal(String.valueOf(a));
        BigDecimal d = new BigDecimal(String.valueOf(b));
        return  c.subtract(d).floatValue();
    }
    public static Double  divide(Double a, Double b){
        BigDecimal c = new BigDecimal(String.valueOf(a));
        BigDecimal d = new BigDecimal(String.valueOf(b));
        return  c.divide(d,4,BigDecimal.ROUND_HALF_UP).doubleValue();//除发要精确位数 否则会崩溃
    }
    public static Float  divide(Float a, Float b){
        BigDecimal c = new BigDecimal(String.valueOf(a));
        BigDecimal d = new BigDecimal(String.valueOf(b));
        return  c.divide(d,4,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * double 转string会有多余的0
     * @param d doblue 有浮点数
     * @return
     */
    public static String doubleToString(String d){

        DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        try {

           // return NumberFormat.getInstance().format(Double.parseDouble(d));
            return decimalFormat.format(Double.parseDouble(d));
        }catch (Exception e){
            return d;
        }
    }
    public static String doubleToString(Double d){

       DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
        try {
            //return NumberFormat.getInstance().format(d);  //这个比如1000 会显示成 1,000
           return decimalFormat.format(d);
        }catch (Exception e){
            return d+"";
        }
    }

    /**
     * 分转换元
     * @param price
     * @return
     */
    public static String changeF2Y(int price) {
        return BigDecimal.valueOf(price).divide(new BigDecimal(100)).toString();
    }

}
